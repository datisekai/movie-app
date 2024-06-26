import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  Unique,
  CreateDateColumn,
  UpdateDateColumn,
  BeforeInsert,
  BeforeUpdate,
  OneToMany,
} from 'typeorm';
import { hash } from 'bcryptjs';
import { Comment as CommentEntity } from '../comment/comment.entity';
import { Order as OrderEntity } from '../order/order.entity';
import { Favourite as FavouriteEntity } from '../favourite/favourite.entity';

@Entity()
@Unique(['email'])
export class User {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  email: string;

  @Column()
  fullname: string;

  @Column({ nullable: true, select: false })
  fullname_search: string;

  @Column({ select: false, nullable: true })
  password: string;

  @Column({ type: 'simple-array' })
  roles: string[];

  @Column({ default: true })
  is_active: boolean;

  @Column({ default: false, select: false })
  is_deleted: boolean;

  @CreateDateColumn()
  created_at: Date;

  @UpdateDateColumn()
  updated_at: Date;

  @Column({ nullable: true })
  fcmToken: string;

  @OneToMany(() => CommentEntity, (comment) => comment.user)
  comments!: CommentEntity[];

  @OneToMany(() => OrderEntity, (order) => order.user)
  orders!: OrderEntity[];

  @OneToMany(() => FavouriteEntity, (favourite) => favourite.user)
  favourites!: FavouriteEntity[];

  @BeforeInsert()
  @BeforeUpdate()
  async hashPassword() {
    if (!this.password) {
      return;
    }
    this.password = await hash(this.password, 10);
  }
}
