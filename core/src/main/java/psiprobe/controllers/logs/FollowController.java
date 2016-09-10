/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.controllers.logs;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.tools.BackwardsFileStream;
import psiprobe.tools.BackwardsLineReader;
import psiprobe.tools.logging.LogDestination;

import java.io.File;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class FollowController.
 *
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class FollowController extends LogHandlerController {

  @Override
  protected ModelAndView handleLogFile(HttpServletRequest request, HttpServletResponse response,
      LogDestination logDest) throws Exception {

    ModelAndView mv = new ModelAndView(getViewName());
    File file = logDest.getFile();

    if (file.exists()) {
      LinkedList<String> lines = new LinkedList<>();
      long actualLength = file.length();
      long lastKnownLength = ServletRequestUtils.getLongParameter(request, "lastKnownLength", 0);
      long currentLength =
          ServletRequestUtils.getLongParameter(request, "currentLength", actualLength);
      long maxReadLines = ServletRequestUtils.getLongParameter(request, "maxReadLines", 0);

      if (lastKnownLength > currentLength || lastKnownLength > actualLength
          || currentLength > actualLength) {

        // file length got reset
        lastKnownLength = 0;
        lines.add(" ------------- THE FILE HAS BEEN TRUNCATED --------------");
      }

      try (BackwardsFileStream bfs = new BackwardsFileStream(file, currentLength)) {
        BackwardsLineReader br;
        if (logDest.getEncoding() != null) {
          br = new BackwardsLineReader(bfs, logDest.getEncoding());
        } else {
          br = new BackwardsLineReader(bfs);
        }
        long readSize = 0;
        long totalReadSize = currentLength - lastKnownLength;
        String line;
        while (readSize < totalReadSize && (line = br.readLine()) != null) {
          if (!line.isEmpty()) {
            lines.addFirst(line);
            readSize += line.length();
          } else {
            readSize++;
          }
          if (maxReadLines != 0 && lines.size() >= maxReadLines) {
            break;
          }
        }

        if (lastKnownLength != 0 && readSize > totalReadSize) {
          lines.removeFirst();
        }
      }

      mv.addObject("lines", lines);
    }
    return mv;
  }

}
