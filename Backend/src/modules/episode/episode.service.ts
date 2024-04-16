import {
  Injectable,
  NotFoundException,
  BadRequestException,
  Query,
} from '@nestjs/common';
import { In, Repository } from 'typeorm';
import { InjectRepository } from '@nestjs/typeorm';
import { removeVietnameseDiacritics } from 'src/common/helpers';
import { convertToSlug } from 'src/common/helpers/convertToSlug';
import { CategoryService } from '../category/category.service';
import { Category } from '../category/category.entity';
import { Episode } from './episode.entity';
import {
  CreateEpisodeDto,
  EditEpisodeDto,
  EpisodeUpdatePositionDto,
} from './episode.dto';
import { FilmService } from '../film/film.service';
import { FirebaseService } from '../firebase/firebase.service';

export interface EpisodeFindOne {
  id?: number;
  slug?: string;
}

@Injectable()
export class EpisodeService {
  constructor(
    @InjectRepository(Episode)
    private readonly episodeRepository: Repository<Episode>,
    private readonly filmService: FilmService,
    private readonly firebaseService: FirebaseService,
  ) {}

  async getMany(query: any, filmId: number) {
    const page = +query.page || 1;
    const limit = +query.limit || 10;

    const queryBuilder = await this.episodeRepository
      .createQueryBuilder('episode')
      .where('episode.is_deleted = false')
      .andWhere('episode.film.id = :filmId', { filmId })
      .orderBy('episode.position', 'ASC')
      .take(limit)
      .skip((page - 1) * limit);

    if (query.title) {
      queryBuilder.andWhere('episode.title_search like :title', {
        title: `%${query.title.toLowerCase()}%`,
      });
    }

    if (query.is_active) {
      queryBuilder.andWhere('episode.is_active = :is_active', {
        is_active: query.is_active,
      });
    }

    const [data, totalEntries] = await queryBuilder.getManyAndCount();

    return { data, totalEntries, page, limit };
  }

  async getOne(id: number, episodeEntity?: Episode) {
    const episode = await this.episodeRepository
      .findOne({ where: { id, is_deleted: false } })
      .then((u) =>
        !episodeEntity ? u : !!u && episodeEntity.id === u.id ? u : null,
      );

    if (!episode)
      throw new NotFoundException('Episode does not exists or unauthorized');

    return episode;
  }

  async createOne(dto: CreateEpisodeDto) {
    if (dto.slug) {
      const isExisted = await this.findOne({ slug: dto.slug });
      if (isExisted) throw new BadRequestException('Slug existed');
    }

    const currentFilm = await this.filmService.findOne({ id: dto.film_id });
    if (!currentFilm) throw new BadRequestException('Film does not exists');

    const newEpisode = this.episodeRepository.create({
      ...dto,
      slug: dto.slug
        ? convertToSlug(dto.slug)
        : `${convertToSlug(dto.title)}-${Date.now()}`,
      title_search: removeVietnameseDiacritics(dto.title),
      position: dto.position || 1,
      film: currentFilm,
    });

    const episode = await this.episodeRepository.save(newEpisode);

    this.firebaseService.sendNotificationToFavoriteUsers(currentFilm.id);

    return episode;
  }

  async editOne(id: number, dto: EditEpisodeDto, episodeEntity?: Episode) {
    const episode = await this.getOne(id, episodeEntity);
    episode.description = dto.description || episode.description;
    episode.thumbnail = dto.thumbnail || episode.thumbnail;
    episode.slug = dto.slug ? convertToSlug(dto.slug) : episode.slug;
    episode.position = dto.position || episode.position;
    episode.url = dto.url || episode.url;
    if (dto.title) {
      episode.title = dto.title;
      episode.title_search = removeVietnameseDiacritics(
        dto.title,
      ).toLocaleLowerCase();
    }

    if (dto.is_active != null) {
      episode.is_active = dto.is_active;
    }

    if (dto.film_id) {
      const currentFilm = await this.filmService.findOne({ id: dto.film_id });
      if (!currentFilm) throw new BadRequestException('Film does not exists');

      episode.film = currentFilm;
    }

    return await this.episodeRepository.save(episode);
  }

  async deleteOne(id: number, episodeEntity?: Episode) {
    const removedFilm = await this.getOne(id, episodeEntity);
    removedFilm.is_deleted = true;
    return await this.episodeRepository.save(removedFilm);
  }

  async findOne(data: EpisodeFindOne) {
    return await this.episodeRepository
      .createQueryBuilder('film')
      .where({ ...data, is_deleted: false })
      .getOne();
  }

  async updatePosition(episode_update_position_dto: EpisodeUpdatePositionDto) {
    const listPositionUpdate = episode_update_position_dto.positions;
    for (const positionUpdate of listPositionUpdate) {
      const { id, position } = positionUpdate;
      await this.episodeRepository.update(id, { position });
    }
    return true;
  }

  async findEpisodesByIds(ids: number[]) {
    return await this.episodeRepository
      .createQueryBuilder('episode')
      .where({ id: In(ids), is_deleted: false })
      .leftJoinAndSelect('episode.film', 'film')
      .getMany();
  }
}
