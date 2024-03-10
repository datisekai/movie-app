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
import { FilmType } from 'src/app.role';
import { EnumToString } from 'src/common/helpers';

enum FilmStatus {
  UPDATING = 'updating',
  FULL = 'full',
}
export class FilmDto {
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

  @ApiProperty()
  @IsEnum(FilmType, {
    message: `must be a valid type value, ${EnumToString(FilmType)}`,
  })
  type: string;

  @ApiProperty()
  @IsEnum(FilmStatus, {
    message: `must be a valid status value, ${EnumToString(FilmStatus)}`,
  })
  status: string;

  @IsOptional()
  @ApiPropertyOptional()
  @IsBoolean()
  is_required_premium: boolean;

  @IsOptional()
  @ApiPropertyOptional()
  @IsString()
  director: string;

  @IsOptional()
  @ApiPropertyOptional()
  @IsString()
  location: string;

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

export class CreateFilmDto extends FilmDto {}
export class EditFilmDto extends PartialType(FilmDto) {}
