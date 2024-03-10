import {
  Injectable,
  NotFoundException,
  BadRequestException,
  Query,
} from '@nestjs/common';
import { Repository } from 'typeorm';
import { InjectRepository } from '@nestjs/typeorm';
import { Category } from './category.entity';
import { removeVietnameseDiacritics } from 'src/common/helpers';
import { CreateCategoryDto, EditCategoryDto } from './category.dto';
import { convertToSlug } from 'src/common/helpers/convertToSlug';

export interface CategoryFindOne {
  id?: number;
}

@Injectable()
export class CategoryService {
  constructor(
    @InjectRepository(Category)
    private readonly categoryRepository: Repository<Category>,
  ) {}

  async getMany(query: any) {
    const page = +query.page || 1;
    const limit = +query.limit || 10;

    const queryBuilder = await this.categoryRepository
      .createQueryBuilder('category')
      .where('category.is_deleted = false')
      .take(limit)
      .skip((page - 1) * limit);

    if (query.title) {
      queryBuilder.andWhere('category.title_search like :title', {
        title: `%${query.title.toLowerCase()}%`,
      });
    }

    if (query.is_active) {
      queryBuilder.andWhere('category.is_active = :is_active', {
        is_active: query.is_active,
      });
    }

    const [data, totalEntries] = await queryBuilder.getManyAndCount();

    return { data, totalEntries, page, limit };
  }

  async getOne(id: number, categoryEntity?: Category) {
    const category = await this.categoryRepository
      .findOne({ where: { id, is_deleted: false } })
      .then((u) =>
        !categoryEntity ? u : !!u && categoryEntity.id === u.id ? u : null,
      );

    if (!category)
      throw new NotFoundException('Category does not exists or unauthorized');

    return category;
  }

  async createOne(dto: CreateCategoryDto) {
    const newCategory = this.categoryRepository.create({
      ...dto,
      title_search: removeVietnameseDiacritics(dto.title),
      slug: dto.slug ? convertToSlug(dto.slug) : convertToSlug(dto.title),
    });
    const category = await this.categoryRepository.save(newCategory);

    return category;
  }

  async editOne(id: number, dto: EditCategoryDto, categoryEntity?: Category) {
    const category = await this.getOne(id, categoryEntity);
    category.description = dto.description || category.description;
    category.thumbnail = dto.thumbnail || category.thumbnail;
    category.slug = dto.slug ? convertToSlug(dto.slug) : category.slug;
    if (dto.title) {
      category.title = dto.title;
      category.title_search = removeVietnameseDiacritics(
        dto.title,
      ).toLocaleLowerCase();
    }

    if (dto.is_active != null) {
      category.is_active = dto.is_active;
    }

    return await this.categoryRepository.save(category);
  }

  async deleteOne(id: number, categoryEntity?: Category) {
    const removedCategory = await this.getOne(id, categoryEntity);
    removedCategory.is_deleted = true;
    return await this.categoryRepository.save(removedCategory);
  }

  async findOne(data: CategoryFindOne) {
    return await this.categoryRepository
      .createQueryBuilder('category')
      .where({ ...data, is_deleted: false })
      .getOne();
  }
}
