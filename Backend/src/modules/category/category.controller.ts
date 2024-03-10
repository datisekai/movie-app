import {
  Controller,
  Get,
  Post,
  Put,
  Delete,
  Param,
  Body,
  Query,
} from '@nestjs/common';
import { Auth, User } from 'src/common/decorators';
import { ApiOperation, ApiTags } from '@nestjs/swagger';
import { RolesBuilder, InjectRolesBuilder } from 'nest-access-control';
import { AppResource, AppRoles } from 'src/app.role';
import { CategoryService } from './category.service';
import { CreateCategoryDto, EditCategoryDto } from './category.dto';
import { User as UserEntity } from '../user/user.entity';

@ApiTags(AppResource.CATEGORY)
@Controller('api.category')
export class CategoryController {
  constructor(
    private readonly categoryService: CategoryService,
    @InjectRolesBuilder()
    private readonly rolesBuilder: RolesBuilder,
  ) {}

  @Get()
  @ApiOperation({
    summary: 'Get List Category',
    description: 'Query with title, is_active',
  })
  async getMany(@Query() query) {
    return await this.categoryService.getMany(query);
  }

  @Get(':id')
  @ApiOperation({
    summary: 'Get Category By ID',
  })
  async getOne(@Param('id') id: number) {
    const data = await this.categoryService.getOne(id);
    return { data };
  }

  @Auth({
    possession: 'any',
    action: 'create',
    resource: AppResource.CATEGORY,
  })
  @Post()
  @ApiOperation({
    summary: 'Create Category',
  })
  async createOne(@Body() dto: CreateCategoryDto) {
    const data = await this.categoryService.createOne(dto);
    return { message: 'Category created', data };
  }

  @Auth({
    possession: 'own',
    action: 'update',
    resource: AppResource.CATEGORY,
  })
  @ApiOperation({
    summary: 'Edit Category',
  })
  @Put(':id')
  async editOne(
    @Param('id') id: number,
    @Body() dto: EditCategoryDto,
    @User() user: UserEntity,
  ) {
    const data = await this.categoryService.editOne(id, dto);

    return { message: 'Film edited', data };
  }

  @Auth({
    action: 'delete',
    possession: 'own',
    resource: AppResource.CATEGORY,
  })
  @Delete(':id')
  @ApiOperation({
    summary: 'Soft Delete Category',
  })
  async deleteOne(@Param('id') id: number, @User() user: UserEntity) {
    let data;

    data = await this.categoryService.deleteOne(id);
    return { message: 'Film deleted', data };
  }
}
