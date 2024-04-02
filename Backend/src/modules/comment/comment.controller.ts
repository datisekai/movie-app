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
import { CommentService } from './comment.service';
import { CreateCommentDto, EditCommentDto } from './comment.dto';

@ApiTags(AppResource.COMMENT)
@Controller('api.comment')
export class CommentController {
  constructor(
    private readonly commentService: CommentService,
    @InjectRolesBuilder()
    private readonly rolesBuilder: RolesBuilder,
  ) {}

  @Get('/film/:filmId')
  @ApiOperation({
    summary: 'Get List Comment By FilmId',
  })
  async getMany(@Query() query, @Param('filmId') filmId: number) {
    return await this.commentService.getMany(query, filmId);
  }

  @Auth({
    possession: 'any',
    action: 'create',
    resource: AppResource.COMMENT,
  })
  @Post()
  @ApiOperation({
    summary: 'Create Comment',
  })
  async createOne(@Body() dto: CreateCommentDto, @User() user: UserEntity) {
    const data = await this.commentService.createOne(dto, user);
    return { message: 'Comment created', data };
  }

  @Auth({
    possession: 'own',
    action: 'update',
    resource: AppResource.COMMENT,
  })
  @ApiOperation({
    summary: 'Edit Comment',
  })
  @Put(':id')
  async editOne(@Param('id') id: number, @Body() dto: EditCommentDto) {
    const data = await this.commentService.editOne(id, dto);

    return { message: 'Comment edited', data };
  }

  @Auth({
    action: 'delete',
    possession: 'own',
    resource: AppResource.COMMENT,
  })
  @Delete(':id')
  @ApiOperation({
    summary: 'Soft Delete Comment',
  })
  async deleteOne(@Param('id') id: number) {
    let data;

    data = await this.commentService.deleteOne(id);
    return { message: 'Episode deleted', data };
  }
}
