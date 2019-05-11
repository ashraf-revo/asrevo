import {Base} from "./base";
import {IndexImpl} from "./index-impl";

export class Master extends Base {
  image: string;
  secret: string;
  file: string;
  impls: IndexImpl[];
  time: number;
  resolution: string;
}
