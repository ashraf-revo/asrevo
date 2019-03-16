import {Master} from "./master";

export class UserMediaView {
  id: string;
  userId: string;
  mediaId: string;
  createdDate: Date;
  lastViewDate: Date;
  count: number;
  m: Master;
}
