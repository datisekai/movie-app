import { ApiProperty, ApiPropertyOptional, PartialType } from '@nestjs/swagger';
import { IsBoolean, IsNotEmpty, IsOptional, IsString } from 'class-validator';

export class CategoryDto {
  @IsString()
  @IsNotEmpty()
  @ApiProperty()
  title: string;

  @IsString()
  @ApiPropertyOptional()
  @IsOptional()
  description: string;

  @IsString()
  @IsOptional()
  @ApiPropertyOptional()
  thumbnail: string;

  @IsBoolean()
  @IsOptional()
  @ApiPropertyOptional()
  is_active: boolean;
}

export class CreateCategoryDto extends PartialType(CategoryDto) {}
export class EditCategoryDto extends PartialType(CategoryDto) {
  @IsOptional()
  @ApiPropertyOptional()
  @IsString()
  title: string;
}
