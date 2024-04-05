import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ConfigModule } from '@nestjs/config';
import { DatabaseModule } from './database/database.module';
import { AccessControlModule } from 'nest-access-control';
import { roles } from './app.role';
import { AuthModule } from './modules/auth/auth.module';
import { UserModule } from './modules/user/user.module';
import { join } from 'path';
import { CategoryModule } from './modules/category/category.module';
import { FilmModule } from './modules/film/film.module';
import { EpisodeModule } from './modules/episode/episode.module';
import { ArticleModule } from './modules/article/article.module';
import { UploadModule } from './modules/upload/upload.module';
import { CommentModule } from './modules/comment/comment.module';
import { OrderModule } from './modules/order/order.module';
import { FavouriteModule } from './modules/favourite/favourite.module';

@Module({
  imports: [
    // TypeOrmModule.forRoot(),
    ConfigModule.forRoot(),
    TypeOrmModule.forRoot({
      type: 'mysql',
      host: process.env.DB_HOST,
      port: 3306,
      username: process.env.DB_USER,
      password: process.env.DB_PASSWORD,
      database: process.env.DB_NAME,
      entities: [join(__dirname, '**', '*.entity.{ts,js}')],
      synchronize: true,
      autoLoadEntities: true,
    }),
    AccessControlModule.forRoles(roles),
    AuthModule,
    UserModule,
    CategoryModule,
    FilmModule,
    EpisodeModule,
    ArticleModule,
    UploadModule,
    CommentModule,
    OrderModule,
    FavouriteModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
