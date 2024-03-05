import { Module } from '@nestjs/common';
import { CategoryModule } from '../category/category.module';
import { Episode } from '../episode/episode.entity';
import { Article } from './article.entity';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ArticleController } from './article.controller';
import { ArticleService } from './article.service';

@Module({
  imports: [TypeOrmModule.forFeature([Article]), CategoryModule],
  controllers: [ArticleController],
  providers: [ArticleService],
  exports: [ArticleService],
})
export class ArticleModule {}
