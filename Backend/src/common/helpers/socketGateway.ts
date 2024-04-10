import {
  WebSocketGateway,
  WebSocketServer,
  SubscribeMessage,
  ConnectedSocket,
} from '@nestjs/websockets';
import { Server, Socket } from 'socket.io';

@WebSocketGateway()
export class WebsocketGateway {
  @WebSocketServer()
  server: Server;

  emitNewEpisode(newEpisode) {
    this.server.emit('new-episode', newEpisode);
  }

  @SubscribeMessage('event')
  handleEvent(@ConnectedSocket() client: Socket): void {
    // Emit sự kiện 'notification' khi có event được phát ra
    this.server.emit('notification', { message: 'Có một event mới!' });
  }
}
