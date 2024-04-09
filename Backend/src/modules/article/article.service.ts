import {
  Injectable,
  NotFoundException,
  BadRequestException,
  Query,
} from '@nestjs/common';
import { Repository } from 'typeorm';
import { InjectRepository } from '@nestjs/typeorm';
import { removeVietnameseDiacritics } from 'src/common/helpers';
import { convertToSlug } from 'src/common/helpers/convertToSlug';
import { CategoryService } from '../category/category.service';
import { Category } from '../category/category.entity';
import { Article } from './article.entity';
import { CreateArticleDto, EditArticleDto } from './article.dto';

export interface ArticleFindOne {
  id?: number;
  slug?: string;
}

@Injectable()
export class ArticleService {
  constructor(
    @InjectRepository(Article)
    private readonly articleRepository: Repository<Article>,
    private readonly categoryService: CategoryService,
  ) {}

  async getMany(query: any) {
    const page = +query.page || 1;
    const limit = +query.limit || 10;

    const queryBuilder = await this.articleRepository
      .createQueryBuilder('article')
      .where('article.is_deleted = false')
      .leftJoinAndSelect('article.categories', 'category')
      .take(limit)
      .skip((page - 1) * limit);

    if (query.title) {
      queryBuilder.andWhere('article.title_search like :title', {
        title: `%${query.title.toLowerCase()}%`,
      });
    }

    if (query.is_active) {
      queryBuilder.andWhere('article.is_active = :is_active', {
        is_active: query.is_active,
      });
    }

    const [data, totalEntries] = await queryBuilder.getManyAndCount();

    return { data, totalEntries, page, limit };
  }

  async getOne(id: number, articleEntity?: Article) {
    const article = await this.articleRepository
      .findOne({ where: { id, is_deleted: false }, relations: ['categories'] })
      .then((u) =>
        !articleEntity ? u : !!u && articleEntity.id === u.id ? u : null,
      );

    if (!article)
      throw new NotFoundException('article does not exists or unauthorized');

    return article;
  }

  async createOne(dto: CreateArticleDto) {
    if (dto.slug) {
      const isExisted = await this.findOne({ slug: dto.slug });
      if (isExisted) throw new BadRequestException('Slug existed');
    }

    const newArticle = this.articleRepository.create({
      ...dto,
      slug: dto.slug ? convertToSlug(dto.slug) : convertToSlug(dto.title),
      title_search: removeVietnameseDiacritics(dto.title),
    });

    const categories = [];

    for (const id of dto.categoryIds) {
      const category = await this.categoryService.findOne({ id });
      if (category) {
        categories.push(category);
      }
    }

    newArticle.categories = categories;

    const article = await this.articleRepository.save(newArticle);

    return article;
  }

  async editOne(id: number, dto: EditArticleDto) {
    const article = await this.getOne(id);
    article.description = dto.description || article.description;
    article.thumbnail = dto.thumbnail || article.thumbnail;
    article.slug = dto.slug ? convertToSlug(dto.slug) : article.slug;
    article.thumbnail = dto.thumbnail || article.thumbnail;
    article.content = dto.content || article.content;
    if (dto.title) {
      article.title = dto.title;
      article.title_search = removeVietnameseDiacritics(
        dto.title,
      ).toLocaleLowerCase();
    }

    if (dto.is_active != null) {
      article.is_active = dto.is_active;
    }

    if (dto.categoryIds && dto.categoryIds.length > 0) {
      const categories = [];
      for (const id of dto.categoryIds) {
        const category = await this.categoryService.findOne({ id });
        if (category) {
          categories.push(category);
        }
      }

      article.categories = categories;
    }

    return await this.articleRepository.save(article);
  }

  async deleteOne(id: number) {
    const removedArticle = await this.getOne(id);
    removedArticle.is_deleted = true;
    return await this.articleRepository.save(removedArticle);
  }

  async findOne(data: ArticleFindOne) {
    return await this.articleRepository
      .createQueryBuilder('article')
      .where({ ...data, is_deleted: false })
      .getOne();
  }
}
