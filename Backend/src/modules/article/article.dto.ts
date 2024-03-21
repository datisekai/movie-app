import { ApiProperty, ApiPropertyOptional, PartialType } from '@nestjs/swagger';
import {
  ArrayMinSize,
  IsArray,
  IsBoolean,
  IsEnum,
  IsNotEmpty,
  IsNumber,
  IsOptional,
  IsString,
} from 'class-validator';
import { EnumToString } from 'src/common/helpers';

export class ArticleDto {
  @IsOptional()
  @ApiPropertyOptional()
  @IsString()
  slug: string;

  @IsNotEmpty()
  @ApiProperty()
  @IsString()
  title: string;

  @IsOptional()
  @ApiPropertyOptional()
  @IsString()
  description: string;

  @IsNotEmpty()
  @ApiProperty()
  @IsString()
  thumbnail: string;

  @IsNotEmpty()
  @ApiProperty()
  @IsString()
  content: string;

  @IsOptional()
  @ApiPropertyOptional()
  @IsBoolean()
  is_active: boolean;

  @ApiProperty()
  @ArrayMinSize(1)
  @IsNumber({}, { each: true })
  @IsArray()
  categoryIds: number[];
}

export class CreateArticleDto extends ArticleDto {}
export class EditArticleDto extends PartialType(ArticleDto) {}
