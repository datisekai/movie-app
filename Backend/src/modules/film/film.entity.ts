import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
  ManyToMany,
  JoinTable,
  OneToMany,
} from 'typeorm';
import { Category } from '../category/category.entity';
import { Episode as EpisodeEntity } from '../episode/episode.entity';

@Entity()
export class Film {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ unique: true })
  slug: string;

  @Column()
  title: string;

  @Column({ nullable: true })
  title_search: string;

  @Column({ nullable: true })
  description: string;

  @Column({ default: 0 })
  view: number;

  @Column({ nullable: true })
  thumbnail: string;

  @Column()
  type: string;

  @Column()
  status: string;

  @Column({ default: false })
  is_required_premium: boolean;

  @Column({ nullable: true })
  director: string;

  @Column({ nullable: true })
  location: string;

  @ManyToMany(() => Category)
  @JoinTable()
  categories: Category[];

  @Column({ default: true })
  is_active: boolean;

  @Column({ default: false, select: false })
  is_deleted: boolean;

  @CreateDateColumn()
  created_at: Date;

  @UpdateDateColumn()
  updated_at: Date;

  @OneToMany(() => EpisodeEntity, (episode) => episode.film)
  episodes!: EpisodeEntity[];
}
