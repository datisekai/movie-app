import { Injectable } from '@nestjs/common';
import * as admin from 'firebase-admin';
import { FilmService } from '../film/film.service';
import { UserService } from '../user/user.service';

admin.initializeApp({
  credential: admin.credential.cert({
    clientEmail:
      'firebase-adminsdk-qaln0@movieapp-18b2e.iam.gserviceaccount.com',
    privateKey:
      '-----BEGIN PRIVATE KEY-----\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCQeNT+r8gM9471\n1fK/7RTyFY4AGyer6LnQb+LfplwKg1Ehf0UhvVuortxkGSG2LtAS2QmexQ0gjtco\n7SvUiI9CUV/RweYRf3M1ltE+QPzZsXN/uspIfOM521Hhqzy9HNxU+/VGGzlbV1RF\nIj0VYaqk+5GOhuNmfWIMWaPpZTweCue7Ob+fh4q9WPSdVKxs84QSVgbYb0axdELy\nTQH3jXr4xPvBavNAtZZjzbzk7CdJajy5Tmhw0UsYyiCiATq8ZNKPowqb4HTzbmeg\nR/rQqxHZJgUuai1xn0slziZ1a81vDnVCetCuD14ssF+hQoy7wvnOChRjGjWXM7/i\n84a8Q1YTAgMBAAECggEAC5S8Ejx6D1wK6DcB0hPJsYw7yVNj7zplJsnJSH9X8nnL\nV6GRo54/yjpWLWn8U1PWVyRxynnmTv1UTxIQCOX3gevu/+wIqs9qJ1oTJOflvPem\nGA+bporoVht4KVxmLoyxNtjJtRh6lRFfjnkrp1qgaMBHz0KN2QnlAbNv2cZFNu0u\n/3YR6PvLf9or60RFtCbrqJkz4PcgkmUykd0/DiKfdnfLyub29DiteKI5K2QXTlov\nKOUhEsy5Izp4FxFkC2oAZTqZ9InTI8Q6DfuTQE756ooywz3+gG50Zm2cKjmo9Xhm\nh82tdECnM4RmBbdtTR8eboB91jx7gPuLcKggEkqH0QKBgQDJiw4oHGf/89/mcie4\n3vlL8WL3GwInEZqNdkf84BQ9+eGk7eDzAogz04gwTmPrU2JVy36/3PjUdtidDRSB\n+0hfhz9g4kyGCxdF4I9+dyqXd/BCyekMux6F4YOFu3joAjRlMCMZ9ifUksiRA3OP\nypqYgcjWAOJu1sTb+7EDc/W7wwKBgQC3ghsBeMIas8Ft/4E0u9/dqV/NYvvymOrv\nvMo7VVOv/h7AgsHsynBBuqgjbKLz5JxOkpNbfbU8Lxt6lt/y+JyXtvyqFWUSO2G9\nO/luP4Sb7dykkrY04/g9K9Qvuxo8eVVZIahr5dOiP/6vAoKnDdbBdaE/nXtNu9/l\nxQYy9MBncQKBgQC7nL1161OIuS2xKIz/lFj/L9pPaw3q7oEvtG644KMXOlipnmLr\n9GxzyjMZvK9Y7cs5UHBSDFJ5/ZQRRo7TnOY3PwBpoN9ZXFOy0aLiN+4nCBhZI/nU\nxw9X06EKSYT4fKznkRLr5PAY+vbHvy3BKfoVg6NAS5x9T2H74eVa8IRM3wKBgFYQ\n1juleFvvrKu7ZszlxIQAmScJ4qKQJ7fSsoKLbVmgdKnTacJ125poQc2DeRWsRcbw\nJd6GIccdTOT8+AHLqh3Zu7zFlgasBRYsl7qmXbqIH7CsAnrh5J5D/Rt5xD9SpL3N\n9GDOhKiXHQrVwZM6AVvwoHTmllsJ32OzOUoS0l5xAoGBAIfSyr0RuFXKAdpcDqR6\n32sLm3/3d/loL+0kNz0AMW5Udp60Jr/nhc9roTFQlX+4qJgkSVbfbSmEzW7Vxhwg\nfsq+s7nhXATp+14lP91jC0xHhwpUXhyV6Bbc4AoWPHN1NmVO9yDf6honHDEbevnU\nOKLV4L1kkM4q5Kw3FumGRFMt\n-----END PRIVATE KEY-----\n',
    projectId: 'movieapp-18b2e',
  }),
});

@Injectable()
export class FirebaseService {
  constructor(
    private readonly filmService: FilmService,
    private readonly userService: UserService,
  ) {}

  async sendNotificationToFavoriteUsers(filmId: number) {
    const film = await this.filmService.getOne(filmId);
    const favoriteUsers =
      await this.userService.getUsersWhoFavoredMovie(filmId);

    console.log('film', film);
    console.log('favourite user', favoriteUsers);

    for (const user of favoriteUsers) {
      if (user.fcmToken) {
        await this.sendNotificationToUser(
          'Phim yêu thích của bạn vừa có tập mới! Vào xem nhanh nào!!!!!',
          `${film.title} - đầy hấp dẫn`,
          film.id.toString(),
          user.fcmToken,
        );
      }
    }
  }

  async sendNotification(fcmToken: string) {
    try {
      await admin.messaging().send({
        token: fcmToken,
        notification: {
          title: 'New Movie Added',
          body: 'Your favorite movie has been added to the catalog.',
        },
      });
      return true;
    } catch (error) {
      console.log(error);
      return false;
    }
  }

  async sendNotificationToUser(
    title: string,
    body: string,
    data: any,
    token: string,
  ) {
    try {
      await admin.messaging().send({
        token: token,
        notification: {
          title,
          body,
        },
        data: {
          id: data,
        },
      });
      console.log('Notification sent successfully');
    } catch (error) {
      console.error('Error sending notification:', error);
    }
  }
}
