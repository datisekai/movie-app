import { BadRequestException, Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { FilmService } from '../film/film.service';
import { UserService } from '../user/user.service';
import { Favourite } from './favourite.entity';
import { FavouriteDto } from './favourite.dto';

@Injectable()
export class FavouriteService {
  constructor(
    @InjectRepository(Favourite)
    private readonly favouriteRepository: Repository<Favourite>,
    private readonly filmService: FilmService,
    private readonly userService: UserService,
  ) {}

  async getMyFavourite(query: any, user: any) {
    const page = +query.page || 1;
    const limit = +query.limit || 10;

    const queryBuilder = await this.favouriteRepository
      .createQueryBuilder('favourite')
      .andWhere('favourite.user.id = :userId', { userId: user.id })
      .orderBy('favourite.created_at', 'DESC')
      .leftJoinAndSelect('favourite.film', 'film')
      .take(limit)
      .skip((page - 1) * limit);

    const [data, totalEntries] = await queryBuilder.getManyAndCount();

    return { data, totalEntries, page, limit };
  }

  async createOne(dto: FavouriteDto, user: any) {
    const currentFilm = await this.filmService.findOne({
      id: dto.film_id,
    });
    if (!currentFilm) throw new BadRequestException('Film does not exists');

    const queryBuilder = await this.favouriteRepository
      .createQueryBuilder('favourite')
      .andWhere('favourite.user.id = :userId', { userId: user.id })
      .andWhere('favourite.film.id = :filmId', { filmId: currentFilm.id });

    const favourite = await queryBuilder.getOne();

    if (!favourite) {
      const newFavourite = {
        film: currentFilm,
        user: user,
      };
      await this.favouriteRepository.save(newFavourite);
      return newFavourite;
    }

    await this.favouriteRepository.remove(favourite);

    return favourite;
  }
}
