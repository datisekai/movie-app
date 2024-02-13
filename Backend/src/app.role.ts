import { RolesBuilder } from 'nest-access-control';

export enum AppRoles {
  FREE = 'free_user',
  PREMIUM = 'premium_user',
  ADMIN = 'admin',
}

export enum AppResource {
  USER = 'USER',
}

export const roles: RolesBuilder = new RolesBuilder();

roles
  // USER ROLES
  .grant(AppRoles.FREE)
  .updateOwn([AppResource.USER])
  .deleteOwn([AppResource.USER])
  .grant(AppRoles.PREMIUM)
  .extend(AppRoles.FREE)
  // ADMIN ROLES
  .grant(AppRoles.ADMIN)
  .extend(AppRoles.PREMIUM)
  .createAny([AppResource.USER])
  .updateAny([AppResource.USER]);
