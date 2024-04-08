import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { EpisodeModule } from '../episode/episode.module';
import { UserModule } from '../user/user.module';
import { FilmModule } from '../film/film.module';
import { Favourite } from './favourite.entity';
import { FavouriteController } from './favourite.controller';
import { FavouriteService } from './favourite.service';

@Module({
  imports: [TypeOrmModule.forFeature([Favourite]), FilmModule, UserModule],
  controllers: [FavouriteController],
  providers: [FavouriteService],
  exports: [FavouriteService],
})
export class FavouriteModule {}
