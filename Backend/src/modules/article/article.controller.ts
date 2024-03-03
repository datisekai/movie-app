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
import { User as UserEntity } from '../user/user.entity';
import { ArticleService } from './article.service';
import { ArticleDto, CreateArticleDto } from './article.dto';

@ApiTags(AppResource.ARTICLE)
@Controller('api.article')
export class ArticleController {
  constructor(
    private readonly articleService: ArticleService,
    @InjectRolesBuilder()
    private readonly rolesBuilder: RolesBuilder,
  ) {}

  @Get()
  @ApiOperation({
    summary: 'Get List Article',
    description: 'Query with title, is_active',
  })
  async getMany(@Query() query) {
    return await this.articleService.getMany(query);
  }

  @Get(':id')
  @ApiOperation({
    summary: 'Get Article By ID',
  })
  async getOne(@Param('id') id: number) {
    const data = await this.articleService.getOne(id);
    return { data };
  }

  @Auth({
    possession: 'any',
    action: 'create',
    resource: AppResource.ARTICLE,
  })
  @Post()
  @ApiOperation({
    summary: 'Create Article',
  })
  async createOne(@Body() dto: CreateArticleDto) {
    const data = await this.articleService.createOne(dto);
    return { message: 'Article created', data };
  }

  @Auth({
    possession: 'own',
    action: 'update',
    resource: AppResource.ARTICLE,
  })
  @ApiOperation({
    summary: 'Edit Article',
  })
  @Put(':id')
  async editOne(
    @Param('id') id: number,
    @Body() dto: ArticleDto,
    @User() user: UserEntity,
  ) {
    const data = await this.articleService.editOne(id, dto);

    return { message: 'Article edited', data };
  }

  @Auth({
    action: 'delete',
    possession: 'own',
    resource: AppResource.ARTICLE,
  })
  @Delete(':id')
  @ApiOperation({
    summary: 'Soft Delete Article',
  })
  async deleteOne(@Param('id') id: number, @User() user: UserEntity) {
    let data;

    data = await this.articleService.deleteOne(id);
    return { message: 'Article deleted', data };
  }
}
