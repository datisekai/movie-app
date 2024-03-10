import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Film } from './film.entity';
import { FilmController } from './film.controller';
import { FilmService } from './film.service';
import { CategoryModule } from '../category/category.module';
import { Episode } from '../episode/episode.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Episode, Film]), CategoryModule],
  controllers: [FilmController],
  providers: [FilmService],
  exports: [FilmService],
})
export class FilmModule {}
