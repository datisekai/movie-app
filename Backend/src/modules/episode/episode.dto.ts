import { ApiProperty, ApiPropertyOptional, PartialType } from '@nestjs/swagger';
import {
  IsBoolean,
  IsNotEmpty,
  IsNumber,
  IsOptional,
  IsString,
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
  positions: UpdatePositionEpisode[];
}

export class EpisodeHistoryDto {
  episode_ids: number[];
}

class UpdatePositionEpisode {
  id: number;
  position: number;
}

export class CreateEpisodeDto extends EpisodeDto {}
export class EditEpisodeDto extends PartialType(EpisodeDto) {}
