import {Base} from "./base";
import {IndexImpl} from "./index-impl";
import {MediaInformation} from "./media-information";

export class Master extends Base {
  image: string;
  secret: string;
  file: string;
  impls: IndexImpl[];
  time: number;
  resolution: string;
  mediaInfo: MediaInformation;
}
