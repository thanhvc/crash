/*
 * Copyright (C) 2003-2013 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package crash.fsub

import org.crsh.cli.Argument
import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.crsh.cli.Option
import org.crsh.command.CRaSHCommand

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.github.dualsub.srt.DualSrt;
import com.github.dualsub.srt.Entry;
import com.github.dualsub.srt.Merger;
import com.github.dualsub.srt.Srt;
import com.github.dualsub.srt.SrtUtils;
import com.github.dualsub.util.Charset;
import com.github.dualsub.util.Log;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 29, 2013  
 */
@Usage("Film subtitle commands")
class fsub extends CRaSHCommand {
  private static Srt srtLeft;
  private static Srt srtRight;

  @Usage("Loading the subtitle file: fsub load -l [-r] /Users/thanhvc/Documents/english/Friends/season5/Friends_S502_eng.srt")
  @Command
  public Object load(@Usage("The left file path") @Option(names=["l","left"]) String leftFilePath,
                    @Usage("The right file path") @Option(names=["r","right"]) String rightFilePath) 
                     throws IOException {

    if (leftFilePath != null && leftFilePath.length() > 0) {
      srtLeft = new Srt(leftFilePath);
    }
    if (rightFilePath != null && rightFilePath.length() > 0) {
      srtRight = new Srt(rightFilePath);
    }
    
    return "sucesseful";
  }

  @Usage("Gets the size of the subtitle: Commands: fsub size -l or fsub size -r")
  @Command
  public Object size(@Usage("The left file path") @Option(names=["l","left"]) Boolean left,
                    @Usage("The right file path") @Option(names=["r","right"]) Boolean right) 
                     throws ScriptException {

    if (left != null && left && srtLeft != null) {
      return "size of the left: " + srtLeft.getSubtitles().size();
    } else if (srtLeft == null) {
      return "srt left is not found or unloaded!";
    }

    if (right != null && right && srtRight != null) {
      return "size of the right: " + srtRight.getSubtitles().size();
    } else if (srtRight == null) {
      return "srt right is not found or unloaded!";
    }
    return "nothing";
  }

  @Usage("Show all the elements of the subtitle. Commands: fsub log -l OR fsub log -r")
  @Command
  public Object log(@Usage("The left file path") @Option(names=["l","left"]) Boolean left,
                    @Usage("The right file path") @Option(names=["r","right"]) Boolean right) 
                     throws ScriptException {
                      
    if (left != null && left && srtLeft != null) {
      srtLeft.log();
      return "success";
    } else if (srtLeft == null) {
      return "srt left is not found or unloaded!";
    }

    if (right != null && right && srtRight != null) {
      srtRight.log();
      return "success";
    } else if (srtRight == null) {
      return "srt right is not found or unloaded!";
    }
    return "nothing";
  }


  @Usage("Show the elements of the subtitle by offset and limit=10: commands: fsub show -l[-r] 0")
  @Command
  public Object show(@Usage("The left file path") @Option(names=["l","left"]) Boolean left,
                    @Usage("The right file path") @Option(names=["r","right"]) Boolean right,
                    @Argument Integer offset) throws ScriptException {


    if (left != null && left && srtLeft != null) {
      showRange(srtLeft, offset, 10);
      return "success";
    } else if (srtLeft == null) {
      return "srt left is not found or unloaded!";
    }

    if (right != null && right && srtRight != null) {
      showRange(srtLeft, offset, 10);
      return "success";
    } else if (srtRight == null) {
      return "srt right is not found or unloaded!";
    }
    return "nothing";
  }

  
  private void showRange(Srt srt, Integer offset, Integer limit) {
    Map<String, Entry> srtMap = srt.getSubtitles();
    Set<String> keys = srtMap.keySet();
    List<String> listKeys = new ArrayList<String>(keys);
    Integer to = Math.min(offset + limit, keys.size());
    println 'offset = ' + offset;
    println 'to = ' + to;
    //
    List<String> subList = listKeys.subList(offset, to);
    //
    Entry list;
    for (String time : subList) {
      list = srtMap.get(time);
      for (int i = 0; i < list.size(); i++) {
        Log.info(time + " " + list.get(i) + " ");
      }
    }
  }
}
