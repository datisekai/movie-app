import {
  Injectable,
  NotFoundException,
  BadRequestException,
  Query,
} from '@nestjs/common';
import { Repository } from 'typeorm';
import { InjectRepository } from '@nestjs/typeorm';
import { User } from './user.entity';
import { CreateUserDto, EditUserDto, UserDto } from './user.dto';
import { removeVietnameseDiacritics } from 'src/common/helpers';

export interface UserFindOne {
  id?: number;
  email?: string;
}

@Injectable()
export class UserService {
  constructor(
    @InjectRepository(User)
    private readonly userRepository: Repository<User>,
  ) {}

  async getMany(query: any) {
    const page = +query.page || 1;
    const limit = +query.limit || 10;

    const queryBuilder = await this.userRepository
      .createQueryBuilder('user')
      .where('user.is_deleted = false')
      .take(limit)
      .skip((page - 1) * limit);

    if (query.fullname) {
      queryBuilder.andWhere('user.fullname_search like :fullname', {
        fullname: `%${query.fullname.toLowerCase()}%`,
      });
    }

    if (query.is_active) {
      queryBuilder.andWhere('user.is_active = :is_active', {
        is_active: query.is_active,
      });
    }

    if (query.email) {
      queryBuilder.andWhere('user.email like :email', {
        email: `%${query.email}%`,
      });
    }

    const [data, totalEntries] = await queryBuilder.getManyAndCount();

    return { data, totalEntries, page, limit };
  }

  async getOne(id: number, userEntity?: User) {
    const user = await this.userRepository
      .findOne({ where: { id, is_deleted: false } })
      .then((u) =>
        !userEntity ? u : !!u && userEntity.id === u.id ? u : null,
      );

    if (!user)
      throw new NotFoundException('User does not exists or unauthorized');

    return user;
  }

  async createOne(dto: CreateUserDto) {
    const userExist = await this.userRepository.findOne({
      where: { email: dto.email },
    });
    if (userExist)
      throw new BadRequestException('User already registered with email');

    const newUser = this.userRepository.create({
      ...dto,
      fullname_search: removeVietnameseDiacritics(dto.fullname),
    });
    const user = await this.userRepository.save(newUser);

    delete user.password;
    return user;
  }

  async editOne(id: number, dto: EditUserDto, userEntity?: User) {
    const user = await this.getOne(id, userEntity);
    user.email = dto.email || user.email;
    if (dto.fullname) {
      user.fullname = dto.fullname;
      user.fullname_search = removeVietnameseDiacritics(
        dto.fullname,
      ).toLocaleLowerCase();
    }

    if (dto.is_active != null) {
      user.is_active = dto.is_active;
    }

    user.roles = dto.roles || user.roles;

    return await this.userRepository.save(user);
  }

  async deleteOne(id: number, userEntity?: User) {
    const removedUser = await this.getOne(id, userEntity);
    removedUser.is_deleted = true;
    return await this.userRepository.save(removedUser);
  }

  async findOne(data: UserFindOne) {
    return await this.userRepository
      .createQueryBuilder('user')
      .where(data)
      .addSelect('user.password')
      .getOne();
  }
}
