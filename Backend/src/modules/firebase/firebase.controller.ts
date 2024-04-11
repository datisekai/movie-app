import {
  Controller,
  Get,
  Post,
  Put,
  Delete,
  Param,
  Body,
  Query,
  NotFoundException,
} from '@nestjs/common';
import { Auth, User } from 'src/common/decorators';
import { ApiOperation, ApiTags } from '@nestjs/swagger';
import { RolesBuilder, InjectRolesBuilder } from 'nest-access-control';
import { AppResource, AppRoles } from 'src/app.role';
import { User as UserEntity } from '../user/user.entity';
import { FirebaseService } from './firebase.service';

@ApiTags(AppResource.FIREBASE)
@Controller('api.firebase')
export class FirebaseController {
  constructor(private readonly firebaseService: FirebaseService) {}

  @Get()
  @ApiOperation({
    summary: 'Test Send Notification All User',
  })
  async sendNotification(@Query() query) {
    if (!query?.fcmToken) {
      return new NotFoundException('fcmToken is required');
    }
    const data = await this.firebaseService.sendNotification(query.fcmToken);
    return { data };
  }
}
