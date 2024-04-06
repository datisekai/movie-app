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
export class Order {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ default: 0 })
  amount: number;

  @Column({
    type: 'enum',
    enum: ['pending', 'completed', 'canceled'],
    default: 'pending',
  })
  order_status: string;

  @Column({
    type: 'enum',
    enum: ['premium_upgrade', 'product_purchase', 'subscription'],
    default: 'premium_upgrade',
  })
  order_type: string;

  @Column({ nullable: true })
  description?: string;

  @Column()
  zalo_trans_id: string;

  @ManyToOne(() => UserEntity, (user) => user.orders)
  user!: UserEntity;

  @CreateDateColumn()
  created_at: Date;

  @UpdateDateColumn()
  updated_at: Date;

  @Column({ default: false, select: false })
  is_deleted: boolean;
}
