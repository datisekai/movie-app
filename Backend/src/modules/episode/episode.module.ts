import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Episode } from './episode.entity';
import { EpisodeController } from './episode.controller';
import { EpisodeService } from './episode.service';
import { FilmModule } from '../film/film.module';
import { FirebaseModule } from '../firebase/firebase.module';

@Module({
  imports: [TypeOrmModule.forFeature([Episode]), FilmModule, FirebaseModule],
  controllers: [EpisodeController],
  providers: [EpisodeService],
  exports: [EpisodeService],
})
export class EpisodeModule {}
