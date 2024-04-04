import { ApiProperty, ApiPropertyOptional, PartialType } from '@nestjs/swagger';
import {
  IsBoolean,
  IsEnum,
  IsNotEmpty,
  IsNumber,
  IsOptional,
  IsString,
} from 'class-validator';
import { OrderType } from './order.constant';

export class OrderDto {
  @ApiPropertyOptional()
  @IsEnum(OrderType, { message: 'Invalid order type' })
  @IsOptional()
  order_type?: OrderType = OrderType.PremiumUpgrade;

  @ApiPropertyOptional()
  @IsString()
  @IsOptional()
  description?: string;
}

export class CreateOrderDto extends OrderDto {}
export class ConfirmOrderDto {
  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  zalo_trans_id: string;
}
