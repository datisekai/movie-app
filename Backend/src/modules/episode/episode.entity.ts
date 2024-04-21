import {
  Column,
  CreateDateColumn,
  Entity,
  ManyToOne,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from 'typeorm';
import { Film as FilmEntity } from '../film/film.entity';

@Entity()
export class Episode {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ unique: true })
  slug: string;

  @Column()
  title: string;

  @Column({ nullable: true })
  title_search: string;

  @Column({ nullable: true, type: 'text' })
  description: string;

  @Column({ default: 0 })
  view: number;

  @Column({ nullable: true })
  thumbnail: string;

  @Column()
  url: string;

  @Column()
  duration: string;

  @Column({ default: 0 })
  position: number;

  @ManyToOne(() => FilmEntity, (film) => film.episodes)
  film!: FilmEntity;

  @Column({ default: true })
  is_active: boolean;

  @Column({ default: false, select: false })
  is_deleted: boolean;

  @CreateDateColumn()
  created_at: Date;

  @UpdateDateColumn()
  updated_at: Date;
}
