import {
  Injectable,
  NotFoundException,
  BadRequestException,
  Query,
} from '@nestjs/common';
import { Repository } from 'typeorm';
import { InjectRepository } from '@nestjs/typeorm';
import { removeVietnameseDiacritics } from 'src/common/helpers';
import { Film } from './film.entity';
import { CreateFilmDto, EditFilmDto } from './film.dto';
import { convertToSlug } from 'src/common/helpers/convertToSlug';
import { CategoryService } from '../category/category.service';
import { Category } from '../category/category.entity';

export interface FilmFindOne {
  id?: number;
  slug?: string;
}

@Injectable()
export class FilmService {
  constructor(
    @InjectRepository(Film)
    private readonly filmRepository: Repository<Film>,
    private readonly categoryService: CategoryService,
  ) {}

  async getMany(query: any) {
    const page = +query.page || 1;
    const limit = +query.limit || 10;

    const queryBuilder = await this.filmRepository
      .createQueryBuilder('film')
      .where('film.is_deleted = false')
      .take(limit)
      .skip((page - 1) * limit);

    if (query.title) {
      queryBuilder.andWhere('film.title_search like :title', {
        title: `%${query.title.toLowerCase()}%`,
      });
    }

    if (query.is_active) {
      queryBuilder.andWhere('film.is_active = :is_active', {
        is_active: query.is_active,
      });
    }

    const [data, totalEntries] = await queryBuilder.getManyAndCount();

    return { data, totalEntries, page, limit };
  }

  async getOne(id: number, filmEntity?: Film) {
    const film = await this.filmRepository
      .findOne({ where: { id, is_deleted: false } })
      .then((u) =>
        !filmEntity ? u : !!u && filmEntity.id === u.id ? u : null,
      );

    if (!film)
      throw new NotFoundException('Film does not exists or unauthorized');

    return film;
  }

  async createOne(dto: CreateFilmDto) {
    if (dto.slug) {
      const isExisted = await this.findOne({ slug: dto.slug });
      if (isExisted) throw new BadRequestException('Slug existed');
    }

    const newFilm = this.filmRepository.create({
      ...dto,
      slug: dto.slug ? convertToSlug(dto.slug) : convertToSlug(dto.title),
      title_search: removeVietnameseDiacritics(dto.title),
    });

    const categories = [];

    for (const id of dto.categoryIds) {
      const category = await this.categoryService.findOne({ id });
      if (category) {
        categories.push(category);
      }
    }

    newFilm.categories = categories;

    const film = await this.filmRepository.save(newFilm);

    return film;
  }

  async editOne(id: number, dto: EditFilmDto, filmEntity?: Film) {
    const film = await this.getOne(id, filmEntity);
    film.description = dto.description || film.description;
    film.thumbnail = dto.thumbnail || film.thumbnail;
    film.director = dto.director || film.director;
    film.location = dto.location || film.location;
    film.slug = dto.slug ? convertToSlug(dto.slug) : film.slug;
    film.status = dto.status || film.status;
    film.thumbnail = dto.thumbnail || film.thumbnail;
    film.type = dto.type || film.type;
    if (dto.title) {
      film.title = dto.title;
      film.title_search = removeVietnameseDiacritics(
        dto.title,
      ).toLocaleLowerCase();
    }

    if (dto.is_active != null) {
      film.is_active = dto.is_active;
    }

    if (dto.is_required_premium != null) {
      film.is_required_premium = dto.is_required_premium;
    }

    if (dto.categoryIds && dto.categoryIds.length > 0) {
      const categories = [];
      for (const id of dto.categoryIds) {
        const category = await this.categoryService.findOne({ id });
        if (category) {
          categories.push(category);
        }
      }

      film.categories = categories;
    }

    return await this.filmRepository.save(film);
  }

  async deleteOne(id: number, filmEntity?: Film) {
    const removedFilm = await this.getOne(id, filmEntity);
    removedFilm.is_deleted = true;
    return await this.filmRepository.save(removedFilm);
  }

  async findOne(data: FilmFindOne) {
    return await this.filmRepository
      .createQueryBuilder('film')
      .where({ ...data, is_deleted: false })
      .getOne();
  }
}
