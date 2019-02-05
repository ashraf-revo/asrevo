import {User} from './user';
import {MediaInformation} from "./media-information";

export class Media {
  id: string;
  m3u8: string;
  image: string;
  mediaInfo: MediaInformation;
  meta: string;
  time: number;
  userId: string;
  status: string;
  createdDate: string;
  publishDate: string;
  title: string;
  url: string;
  user: User;
  group: string;
}
