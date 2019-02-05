import {User} from './user';

export class UserMediaComment {
  id: string;
  userId: string;
  user: User;
  mediaId: string;
  createdDate: Date;
  message: string;
}
