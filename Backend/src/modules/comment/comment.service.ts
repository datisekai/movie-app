import { BadRequestException, Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { FilmService } from '../film/film.service';
import { UserService } from '../user/user.service';
import { CreateCommentDto, EditCommentDto } from './comment.dto';
import { Comment } from './comment.entity';

export interface EpisodeFindOne {
  id?: number;
  slug?: string;
}

@Injectable()
export class CommentService {
  constructor(
    @InjectRepository(Comment)
    private readonly commentRepository: Repository<Comment>,
    private readonly filmService: FilmService,
    private readonly userService: UserService,
  ) {}

  async getMany(query: any, filmId: number) {
    const page = +query.page || 1;
    const limit = +query.limit || 10;

    const queryBuilder = await this.commentRepository
      .createQueryBuilder('comment')
      .where('comment.is_deleted = false')
      .andWhere('comment.film.id = :filmId', { filmId })
      .orderBy('comment.created_at', 'DESC')
      .take(limit)
      .skip((page - 1) * limit);

    const [data, totalEntries] = await queryBuilder.getManyAndCount();

    return { data, totalEntries, page, limit };
  }

  async createOne(dto: CreateCommentDto, user: any) {
    const currentFilm = await this.filmService.findOne({
      id: dto.film_id,
    });
    if (!currentFilm) throw new BadRequestException('Film does not exists');

    const newComment = {
      content: dto.content,
      film: currentFilm,
      user: user,
    };

    const comment = await this.commentRepository.save(newComment);

    return comment;
  }

  async editOne(id: number, dto: EditCommentDto) {
    const comment = await this.commentRepository.findOne({
      where: { id, is_deleted: false },
    });
    comment.content = dto.content || comment.content;
    if (dto.film_id) {
      const currentFilm = await this.filmService.findOne({
        id: dto.film_id,
      });
      if (!currentFilm)
        throw new BadRequestException('Episode does not exists');
      comment.film = currentFilm;
    }

    return await this.commentRepository.save(comment);
  }

  async deleteOne(id: number) {
    const removedFilm = await this.commentRepository.findOne({
      where: { id, is_deleted: false },
    });
    removedFilm.is_deleted = true;
    return await this.commentRepository.save(removedFilm);
  }
}
