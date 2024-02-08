import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';
import {
  IsBoolean,
  IsEmail,
  IsEnum,
  IsNotEmpty,
  IsOptional,
  IsString,
} from 'class-validator';

export class UserDto {
  @IsEmail()
  @IsString()
  @IsOptional()
  @ApiPropertyOptional()
  email: string;

  @IsString()
  @IsOptional()
  @ApiPropertyOptional()
  password: string;

  @IsEnum(['free', 'premium'])
  @IsString()
  @ApiPropertyOptional()
  plan: string;

  @IsEnum(['user', 'admin'])
  @IsString()
  @ApiPropertyOptional()
  role: string;

  @IsBoolean()
  @ApiPropertyOptional()
  is_active: boolean;
}

export class CreateUserDto {
  @IsEmail()
  @IsString()
  @IsOptional()
  @ApiProperty()
  @IsNotEmpty()
  email: string;

  @IsString()
  @IsOptional()
  @ApiProperty()
  @IsNotEmpty()
  password: string;

  @IsEnum(['free', 'premium'])
  @IsString()
  @ApiProperty()
  @IsNotEmpty()
  plan: string;

  @IsEnum(['user', 'admin'])
  @IsString()
  @ApiProperty()
  @IsNotEmpty()
  role: string;
}
