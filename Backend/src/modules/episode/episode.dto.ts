import { ApiProperty, ApiPropertyOptional, PartialType } from '@nestjs/swagger';
import { Type } from 'class-transformer';
import {
  IsArray,
  IsBoolean,
  IsNotEmpty,
  IsNumber,
  IsOptional,
  IsString,
  ValidateNested,
} from 'class-validator';

export class EpisodeDto {
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
  url: string;

  @IsString()
  @IsNotEmpty()
  @ApiProperty()
  duration: string;

  @ApiPropertyOptional()
  @IsNumber()
  @IsOptional()
  position: number;

  @IsOptional()
  @ApiPropertyOptional()
  @IsBoolean()
  is_active: boolean;

  @ApiProperty()
  @IsNotEmpty()
  @IsNumber()
  film_id: number;
}

export class EpisodeUpdatePositionDto {
  @ApiProperty({ type: () => UpdatePositionEpisode, isArray: true })
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => UpdatePositionEpisode)
  positions: UpdatePositionEpisode[];
}

class UpdatePositionEpisode {
  @IsNumber()
  @ApiProperty()
  id: number;

  @IsNumber()
  @ApiProperty()
  position: number;
}
export class EpisodeHistoryDto {
  @ApiProperty({ type: [Number] })
  // @IsNumber({}, { each: true })
  @IsArray()
  episode_ids: number[];
}

export class CreateEpisodeDto extends EpisodeDto {}
export class EditEpisodeDto extends PartialType(EpisodeDto) {}
