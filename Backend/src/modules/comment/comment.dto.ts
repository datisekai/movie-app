import { ApiProperty, ApiPropertyOptional, PartialType } from '@nestjs/swagger';
import {
  IsBoolean,
  IsNotEmpty,
  IsNumber,
  IsOptional,
  IsString,
} from 'class-validator';

export class CommentDto {
  @IsString()
  @IsNotEmpty()
  @ApiProperty()
  content: string;

  @IsNumber()
  @ApiProperty()
  @IsNotEmpty()
  film_id: number;
}

export class CreateCommentDto extends CommentDto {}
export class EditCommentDto extends PartialType(CommentDto) {}
