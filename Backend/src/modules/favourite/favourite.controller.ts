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
import { FavouriteService } from './favourite.service';
import { FavouriteDto } from './favourite.dto';

@ApiTags(AppResource.FAVOURITE)
@Controller('api.favourite')
export class FavouriteController {
  constructor(
    private readonly favouriteService: FavouriteService,
    @InjectRolesBuilder()
    private readonly rolesBuilder: RolesBuilder,
  ) {}

  @Auth({
    possession: 'any',
    action: 'create',
    resource: AppResource.COMMENT,
  })
  @Get('me')
  @ApiOperation({
    summary: 'Get List My Favourite Film',
  })
  async getMany(@Query() query, @User() user: UserEntity) {
    return this.favouriteService.getMyFavourite(query, user);
  }

  @Auth({
    possession: 'any',
    action: 'create',
    resource: AppResource.FAVOURITE,
  })
  @Post('')
  @ApiOperation({
    summary: 'Handle Favourite',
  })
  async createOne(@Body() dto: FavouriteDto, @User() user: UserEntity) {
    const data = await this.favouriteService.createOne(dto, user);
    return { message: 'Favourite done', data };
  }
}
