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
import { FilmService } from './film.service';
import { CreateFilmDto } from './film.dto';
import { Film } from './film.entity';

@ApiTags(AppResource.FILM)
@Controller('api.film')
export class FilmController {
  constructor(
    private readonly filmService: FilmService,
    @InjectRolesBuilder()
    private readonly rolesBuilder: RolesBuilder,
  ) {}

  @Get()
  @ApiOperation({
    summary: 'Get List Film',
    description: 'Query with title, is_active, category_id',
  })
  async getMany(@Query() query) {
    try {
      const films = await this.filmService.getMany(query);
      return films;
    } catch (error) {
      return { data: [] };
    }
  }

  @Get(':id')
  @ApiOperation({
    summary: 'Get Film By ID',
  })
  async getOne(@Param('id') id: number) {
    const data = await this.filmService.getOne(id);
    return { data };
  }

  @Auth({
    possession: 'any',
    action: 'create',
    resource: AppResource.FILM,
  })
  @Post()
  @ApiOperation({
    summary: 'Create Film',
  })
  async createOne(@Body() dto: CreateFilmDto) {
    const data = await this.filmService.createOne(dto);
    return { message: 'Film created', data };
  }

  @Auth({
    possession: 'own',
    action: 'update',
    resource: AppResource.FILM,
  })
  @ApiOperation({
    summary: 'Edit Film',
  })
  @Put(':id')
  async editOne(
    @Param('id') id: number,
    @Body() dto: Film,
    @User() user: UserEntity,
  ) {
    const data = await this.filmService.editOne(id, dto);

    return { message: 'Film edited', data };
  }

  @Auth({
    action: 'delete',
    possession: 'own',
    resource: AppResource.FILM,
  })
  @Delete(':id')
  @ApiOperation({
    summary: 'Soft Delete Film',
  })
  async deleteOne(@Param('id') id: number, @User() user: UserEntity) {
    let data;

    data = await this.filmService.deleteOne(id);
    return { message: 'Film deleted', data };
  }
}
