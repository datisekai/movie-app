import { ApiProperty } from '@nestjs/swagger';
import { IsNotEmpty, IsNumber } from 'class-validator';

export class FavouriteDto {
  @IsNumber()
  @ApiProperty()
  @IsNotEmpty()
  film_id: number;
}
