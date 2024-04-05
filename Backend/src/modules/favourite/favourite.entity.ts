import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
  ManyToMany,
  JoinTable,
  ManyToOne,
} from 'typeorm';
import { User as UserEntity } from '../user/user.entity';
import { Episode as EpisodeEntity } from '../episode/episode.entity';
import { Film as FilmEntity } from '../film/film.entity';

@Entity()
export class Favourite {
  @PrimaryGeneratedColumn()
  id: number;

  @ManyToOne(() => UserEntity, (user) => user.favourites)
  user!: UserEntity;

  @ManyToOne(() => FilmEntity, (film) => film.favourites)
  film!: FilmEntity;

  @CreateDateColumn()
  created_at: Date;

  @UpdateDateColumn()
  updated_at: Date;
}
