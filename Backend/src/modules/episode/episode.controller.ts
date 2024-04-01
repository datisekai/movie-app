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
import { EpisodeService } from './episode.service';
import {
  CreateEpisodeDto,
  EpisodeHistoryDto,
  EpisodeUpdatePositionDto,
} from './episode.dto';
import { Episode } from './episode.entity';

@ApiTags(AppResource.EPISODE)
@Controller('api.episode')
export class EpisodeController {
  constructor(
    private readonly episodeService: EpisodeService,
    @InjectRolesBuilder()
    private readonly rolesBuilder: RolesBuilder,
  ) {}

  @Get('/film/:filmId')
  @ApiOperation({
    summary: 'Get List Episode By FilmId',
    description: 'Query with title, is_active',
  })
  async getMany(@Query() query, @Param('filmId') filmId: number) {
    return await this.episodeService.getMany(query, filmId);
  }

  @Get(':episodeId')
  @ApiOperation({
    summary: 'Get Film By ID',
  })
  async getOne(@Param('episodeId') id: number) {
    const data = await this.episodeService.getOne(id);
    return { data };
  }

  @Auth({
    possession: 'any',
    action: 'create',
    resource: AppResource.EPISODE,
  })
  @Post()
  @ApiOperation({
    summary: 'Create Episode',
  })
  async createOne(@Body() dto: CreateEpisodeDto) {
    const data = await this.episodeService.createOne(dto);
    return { message: 'Episode created', data };
  }

  @Auth({
    possession: 'own',
    action: 'update',
    resource: AppResource.EPISODE,
  })
  @ApiOperation({
    summary: 'Edit Episode',
  })
  @Put(':id')
  async editOne(
    @Param('id') id: number,
    @Body() dto: Episode,
    @User() user: UserEntity,
  ) {
    const data = await this.episodeService.editOne(id, dto);

    return { message: 'Episode edited', data };
  }

  @Auth({
    action: 'delete',
    possession: 'own',
    resource: AppResource.EPISODE,
  })
  @Delete(':id')
  @ApiOperation({
    summary: 'Soft Delete Episode',
  })
  async deleteOne(@Param('id') id: number, @User() user: UserEntity) {
    let data;

    data = await this.episodeService.deleteOne(id);
    return { message: 'Episode deleted', data };
  }

  @Auth({
    possession: 'own',
    action: 'update',
    resource: AppResource.EPISODE,
  })
  @ApiOperation({
    summary: 'Edit Episode',
  })
  @Put('position')
  async updatePosition(@Body() update_position_dto: EpisodeUpdatePositionDto) {
    return this.episodeService.updatePosition(update_position_dto);
  }

  @ApiOperation({
    summary: 'Get Episode History By Episode Ids',
  })
  @Post('history')
  async findEpisodesByIds(@Body() body: EpisodeHistoryDto) {
    const { episode_ids } = body;
    return this.episodeService.findEpisodesByIds(episode_ids);
  }
}
