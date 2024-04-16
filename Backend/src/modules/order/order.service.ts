import { BadRequestException, Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { FilmService } from '../film/film.service';
import { UserService } from '../user/user.service';
import { Order } from './order.entity';
import { ConfirmOrderDto, CreateOrderDto } from './order.dto';
import { ORDER_AMOUNT, OrderStatus, OrderType } from './order.constant';
import {
  zaloPayConfirmOrder,
  zaloPayCreateOrder,
} from 'src/common/helpers/zalopay';
import { AppRoles } from 'src/app.role';

@Injectable()
export class OrderService {
  constructor(
    @InjectRepository(Order)
    private readonly orderRepository: Repository<Order>,
    private readonly userService: UserService,
  ) {}

  async getMany(query: any) {
    const page = +query.page || 1;
    const limit = +query.limit || 10;

    const queryBuilder = await this.orderRepository
      .createQueryBuilder('order')
      .where('order.is_deleted = false')
      .leftJoinAndSelect('order.user', 'user')
      .orderBy('order.created_at', 'DESC')
      .take(limit)
      .skip((page - 1) * limit);

    const [data, totalEntries] = await queryBuilder.getManyAndCount();

    return { data, totalEntries, page, limit };
  }

  async getMyOrder(query: any, user: any) {
    const page = +query.page || 1;
    const limit = +query.limit || 10;

    const queryBuilder = await this.orderRepository
      .createQueryBuilder('order')
      .where('order.is_deleted = false')
      .leftJoinAndSelect('order.user', 'user')
      .where('user.id = :user_id', { user_id: user.id })
      .orderBy('order.created_at', 'DESC')
      .take(limit)
      .skip((page - 1) * limit);

    const [data, totalEntries] = await queryBuilder.getManyAndCount();

    return { data, totalEntries, page, limit };
  }

  async createOne(dto: CreateOrderDto, user: any) {
    const amount = ORDER_AMOUNT[dto.order_type || OrderType.PremiumUpgrade];
    const createdOrderZalo = await zaloPayCreateOrder(amount);

    if (!createdOrderZalo) {
      throw new BadRequestException('Create order failed');
    }

    const newOrder = {
      user,
      description: dto.description,
      order_type: dto.order_type || OrderType.PremiumUpgrade,
      order_status: OrderStatus.Pending,
      amount: amount,
      zalo_trans_id: createdOrderZalo.order.app_trans_id,
    };
    const order = await this.orderRepository.save(newOrder);

    return { token: createdOrderZalo.zp_trans_token, order };
  }

  async confirm(dto: ConfirmOrderDto, user: any) {
    const order = await this.orderRepository.findOne({
      where: { zalo_trans_id: dto.zalo_trans_id },
    });
    if (!order) {
      throw new BadRequestException('Order not found');
    }

    if (order && order.order_status != OrderStatus.Pending) {
      throw new BadRequestException('Order already confirmed');
    }

    const result = await zaloPayConfirmOrder(order.zalo_trans_id);

    if (!result) {
      throw new BadRequestException('Confirm order failed');
    }

    if (result.return_code == 3) {
      order.order_status = OrderStatus.Canceled;
    }

    if (result.return_code == 1) {
      order.order_status = OrderStatus.Completed;
      if (!user.roles.includes(AppRoles.PREMIUM)) {
        await this.userService.editOne(order.user.id, {
          roles: [...user.roles, AppRoles.PREMIUM],
        });
      }

      await this.orderRepository.save(order);
    }

    return order;
  }

  async deleteOne(id: number) {
    const removedOrder = await this.orderRepository.findOne({
      where: { id, is_deleted: false },
    });
    removedOrder.is_deleted = true;
    return await this.orderRepository.save(removedOrder);
  }
}
