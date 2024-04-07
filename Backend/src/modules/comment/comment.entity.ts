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
export class Comment {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  content: string;

  @Column({ default: false, select: false })
  is_deleted: boolean;

  @ManyToOne(() => UserEntity, (user) => user.comments)
  user!: UserEntity;

  @ManyToOne(() => FilmEntity, (film) => film.comments)
  film!: FilmEntity;

  @CreateDateColumn()
  created_at: Date;

  @UpdateDateColumn()
  updated_at: Date;
}
