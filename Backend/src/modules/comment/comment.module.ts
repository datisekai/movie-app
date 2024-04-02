import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { EpisodeModule } from '../episode/episode.module';
import { UserModule } from '../user/user.module';
import { CommentController } from './comment.controller';
import { Comment } from './comment.entity';
import { CommentService } from './comment.service';
import { FilmModule } from '../film/film.module';

@Module({
  imports: [TypeOrmModule.forFeature([Comment]), FilmModule, UserModule],
  controllers: [CommentController],
  providers: [CommentService],
  exports: [CommentService],
})
export class CommentModule {}
