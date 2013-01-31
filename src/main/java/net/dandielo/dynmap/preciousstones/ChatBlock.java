package net.dandielo.dynmap.preciousstones;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author phaed
 */
public class ChatBlock {
	private final int colspacing = 12;
	private static final int lineLength = 320;
	private ArrayList<Double> columnSizes = new ArrayList<Double>();
	private ArrayList<Integer> columnSpaces = new ArrayList<Integer>();
	private ArrayList<String> columnAlignments = new ArrayList<String>();
	private LinkedList<String[]> rows = new LinkedList<String[]>();
	private boolean prefix_used = false;
	private String color = "";
	/**
     *
     */
	public static final Logger log = Logger.getLogger("Minecraft");

	/**
	 * 
	 * @param columnAlignment
	 */
	public void setAlignment(String... columnAlignment) {
		columnAlignments.addAll(Arrays.asList(columnAlignment));
	}

	/**
	 * 
	 * @param columnSpacings
	 */
	public void setSpacing(int... columnSpacings) {
		for (int spacing : columnSpacings) {
			columnSpaces.add(spacing);
		}
	}

	/**
	 * 
	 * @param columnPercentages
	 * @param prefix
	 */
	public void setColumnSizes(String prefix, double... columnPercentages) {
		int ll = lineLength;

		if (prefix != null) {
			ll = lineLength - (int) msgLength(prefix);
		}

		for (double percentage : columnPercentages) {
			columnSizes.add(Math.floor((percentage / 100) * ll));
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasContent() {
		return rows.size() > 0;
	}

	/**
	 * 
	 * @param contents
	 */
	public void addRow(String... contents) {
		rows.add(contents);
	}

	/**
	 * 
	 * @return
	 */
	public int size() {
		return rows.size();
	}

	/**
     *
     */
	public void clear() {
		rows.clear();
	}

	/**
	 * 
	 * @param sender
	 * @param amount
	 * @return
	 */
	public boolean sendBlock(CommandSender sender, int amount) {
		if (sender == null) {
			return false;
		}

		if (rows.size() == 0) {
			return false;
		}

		if (!(sender instanceof Player)) {
			amount = 999;
		}

		// if no column sizes provided them
		// make some up based on the data

		if (columnSizes.isEmpty()) {
			// generate columns sizes

			int col_count = rows.get(0).length;

			for (int i = 0; i < col_count; i++) {
				// add custom column spacing if specified

				int spacing = colspacing;

				if (columnSpaces.size() >= (i + 1)) {
					spacing = columnSpaces.get(i);
				}

				columnSizes.add(getMaxWidth(i) + spacing);
			}
		}

		// size up all sections

		for (int i = 0; i < amount; i++) {
			if (rows.size() == 0) {
				continue;
			}

			String rowstring = "";
			String row[] = rows.pollFirst();

			for (int sid = 0; sid < row.length; sid++) {
				String section = row[sid];
				double colsize = (columnSizes.size() >= (sid + 1)) ? columnSizes
						.get(sid) : 0;
				String align = (columnAlignments.size() >= (sid + 1)) ? columnAlignments
						.get(sid) : "l";

				if (align.equalsIgnoreCase("r")) {
					if (msgLength(section) > colsize) {
						rowstring += cropLeftToFit(section, colsize);
					} else if (msgLength(section) < colsize) {
						rowstring += padLeftToFit(section, colsize);
					}
				} else if (align.equalsIgnoreCase("l")) {
					if (msgLength(section) > colsize) {
						rowstring += cropRightToFit(section, colsize);
					} else if (msgLength(section) < colsize) {
						rowstring += padRightToFit(section, colsize);
					}
				} else if (align.equalsIgnoreCase("c")) {
					if (msgLength(section) > colsize) {
						rowstring += cropRightToFit(section, colsize);
					} else if (msgLength(section) < colsize) {
						rowstring += centerInLineOf(section, colsize);
					}
				} else if (align.equalsIgnoreCase("w")) {
					if (msgLength(section) > colsize) {
						rowstring += section;
					} else if (msgLength(section) < colsize) {
						rowstring += padRightToFit(section, colsize);
					}
				}
			}

			String msg = cropRightToFit(rowstring, lineLength);

			if (color.length() > 0) {
				msg = color + msg;
			}

			sender.sendMessage(msg);
		}

		return rows.size() > 0;
	}

	/**
	 * 
	 * @param sender
	 * @param prefix
	 */
	public void sendBlock(CommandSender sender, String prefix) {
		if (sender == null) {
			return;
		}

		if (rows.size() == 0) {
			return;
		}

		prefix_used = prefix == null ? true : false;

		String empty_prefix = ChatBlock.makeEmpty(prefix);

		// if no column sizes provided them
		// make some up based on the data

		if (columnSizes.isEmpty()) {
			// generate columns sizes

			int col_count = rows.get(0).length;

			for (int i = 0; i < col_count; i++) {
				// add custom column spacing if specified

				int spacing = colspacing;

				if (columnSpaces.size() >= (i + 1)) {
					spacing = columnSpaces.get(i);
				}

				columnSizes.add(getMaxWidth(i) + spacing);
			}
		}

		// size up all sections

		for (String[] row : rows) {
			String rowstring = "";

			for (int sid = 0; sid < row.length; sid++) {
				String section = row[sid];
				double colsize = (columnSizes.size() >= (sid + 1)) ? columnSizes
						.get(sid) : 0;
				String align = (columnAlignments.size() >= (sid + 1)) ? columnAlignments
						.get(sid) : "l";

				if (align.equalsIgnoreCase("r")) {
					if (msgLength(section) > colsize) {
						rowstring += cropLeftToFit(section, colsize);
					} else if (msgLength(section) < colsize) {
						rowstring += padLeftToFit(section, colsize);
					}
				} else if (align.equalsIgnoreCase("l")) {
					if (msgLength(section) > colsize) {
						rowstring += cropRightToFit(section, colsize);
					} else if (msgLength(section) < colsize) {
						rowstring += padRightToFit(section, colsize);
					}
				} else if (align.equalsIgnoreCase("c")) {
					if (msgLength(section) > colsize) {
						rowstring += cropRightToFit(section, colsize);
					} else if (msgLength(section) < colsize) {
						rowstring += centerInLineOf(section, colsize);
					}
				}
			}

			String msg = cropRightToFit((prefix_used ? empty_prefix : prefix)
					+ " " + rowstring, lineLength);

			if (color.length() > 0) {
				msg = color + msg;
			}

			sender.sendMessage(msg);

			prefix_used = true;
		}
	}

	/**
	 * 
	 * @param sender
	 */
	public void sendBlock(CommandSender sender) {
		sendBlock(sender, null);
	}

	/**
	 * Outputs a message to everybody
	 * 
	 * @param sender
	 * @param msg
	 */
	public static void sendMessageAll(CommandSender sender, String msg) {
		sendMessageAll(sender, msg);
	}

	/**
	 * 
	 * @param col
	 * @return
	 */
	public double getMaxWidth(double col) {
		double maxWidth = 0;

		for (String[] row : rows) {
			maxWidth = Math.max(maxWidth, msgLength(row[(int) col]));
		}

		return maxWidth;
	}

	/**
	 * 
	 * @param msg
	 * @return
	 */
	public static String centerInLine(String msg) {
		return centerInLineOf(msg, lineLength);
	}

	/**
	 * 
	 * @param msg
	 * @param lineLength
	 * @return
	 */
	public static String centerInLineOf(String msg, double lineLength) {
		double length = msgLength(msg);
		double diff = lineLength - length;

		// if too big for line return it as is

		if (diff < 0) {
			return msg;
		}

		double sideSpace = diff / 2;

		// pad the left with space

		msg = padLeftToFit(msg, length + sideSpace);

		// pad the right with space

		msg = padRightToFit(msg, length + sideSpace + sideSpace);

		return msg;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String makeEmpty(String str) {
		if (str == null) {
			return "";
		}

		return padLeftToFit("", msgLength(str));
	}

	/**
	 * 
	 * @param msg
	 * @param length
	 * @return
	 */
	public static String cropRightToFit(String msg, double length) {
		if (msg == null || msg.length() == 0 || length == 0) {
			return "";
		}

		while (msgLength(msg) >= length) {
			msg = msg.substring(0, msg.length() - 2);
		}

		return msg;
	}

	/**
	 * 
	 * @param msg
	 * @param length
	 * @return
	 */
	public static String cropLeftToFit(String msg, double length) {
		if (msg == null || msg.length() == 0 || length == 0) {
			return "";
		}

		while (msgLength(msg) >= length) {
			msg = msg.substring(1);
		}

		return msg;
	}

	/**
	 * Padds left til the string is a certain size
	 * 
	 * @param msg
	 * @param length
	 * @return
	 */
	public static String padLeftToFit(String msg, double length) {
		if (msgLength(msg) > length) {
			return msg;
		}

		while (msgLength(msg) < length) {
			msg = " " + msg;
		}

		return msg;
	}

	/**
	 * Pads right til the string is a certain size
	 * 
	 * @param msg
	 * @param length
	 * @return
	 */
	public static String padRightToFit(String msg, double length) {
		if (msgLength(msg) > length) {
			return msg;
		}

		while (msgLength(msg) < length) {
			msg += " ";
		}

		return msg;
	}

	/**
	 * Finds the length on the screen of a string. Ignores colors.
	 * 
	 * @param str
	 * @return
	 */
	public static double msgLength(String str) {
		double length = 0;
		str = cleanColors(str);

		// Loop through all the characters, skipping any color characters and
		// their following color codes

		for (int x = 0; x < str.length(); x++) {
			int len = charLength(str.charAt(x));
			if (len > 0) {
				length += len;
			} else {
				x++;
			}
		}
		return length;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String cleanColors(String str) {
		String patternStr = "ï¿½.";
		String replacementStr = "";

		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(str);
		String out = matcher.replaceAll(replacementStr);

		return out;
	}

	/**
	 * Finds the visual length of the character on the screen.
	 * 
	 * @param x
	 * @return
	 */
	public static int charLength(char x) {
		if ("i.:,;|!".indexOf(x) != -1) {
			return 2;
		} else if ("l'".indexOf(x) != -1) {
			return 3;
		} else if ("tI[]".indexOf(x) != -1) {
			return 4;
		} else if ("fk{}<>\"*()".indexOf(x) != -1) {
			return 5;
		} else if ("abcdeghjmnopqrsuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ1234567890\\/#?$%-=_+&^"
				.indexOf(x) != -1) {
			return 6;
		} else if ("@~".indexOf(x) != -1) {
			return 7;
		} else if (x == ' ') {
			return 4;
		} else {
			return -1;
		}
	}

	/**
	 * 
	 * @param msg
	 * @return
	 */
	public static String[] wordWrap(String msg) {
		return wordWrap(msg, 0);
	}

	/**
	 * Cuts the message apart into whole words short enough to fit on one line
	 * 
	 * @param msg
	 * @param prefixLn
	 * @return
	 */
	public static String[] wordWrap(String msg, int prefixLn) {
		// Split each word apart

		ArrayList<String> split = new ArrayList<String>();
		split.addAll(Arrays.asList(msg.split(" ")));

		// Create an array list for the output

		ArrayList<String> out = new ArrayList<String>();

		// While i is less than the length of the array of words

		while (!split.isEmpty()) {
			int len = 0;

			// Create an array list to hold individual words

			ArrayList<String> words = new ArrayList<String>();

			// Loop through the words finding their length and increasing
			// j, the end point for the sub string

			while (!split.isEmpty() && split.get(0) != null
					&& len <= (lineLength - prefixLn)) {
				double wordLength = msgLength(split.get(0)) + 4;

				// If a word is too long for a line

				if (wordLength > (lineLength - prefixLn)) {
					String[] tempArray = wordCut(len, split.remove(0));
					words.add(tempArray[0]);
					split.add(tempArray[1]);
				}

				// If the word is not too long to fit

				len += wordLength;
				if (len < (lineLength - prefixLn) + 4) {
					words.add(split.remove(0));
				}
			}
			// Merge them and add them to the output array.
			String merged = combineSplit(0,
					words.toArray(new String[words.size()]), " ")
					+ " ";
			out.add(merged.replaceAll("\\s+$", ""));
		}
		// Convert to an array and return

		return out.toArray(new String[out.size()]);
	}

	/**
	 * 
	 * @param startIndex
	 * @param string
	 * @param seperator
	 * @return
	 */
	public static String combineSplit(int startIndex, String[] string,
			String seperator) {
		StringBuilder builder = new StringBuilder();
		for (int i = startIndex; i < string.length; i++) {
			builder.append(string[i]);
			builder.append(seperator);
		}
		builder.deleteCharAt(builder.length() - seperator.length());

		return builder.toString();
	}

	/**
	 * Cuts apart a word that is too long to fit on one line
	 * 
	 * @param lengthBefore
	 * @param str
	 * @return
	 */
	public static String[] wordCut(int lengthBefore, String str) {
		int length = lengthBefore;

		// Loop through all the characters, skipping any color characters and
		// their following color codes

		String[] output = new String[2];
		int x = 0;
		while (length < lineLength && x < str.length()) {
			int len = charLength(str.charAt(x));
			if (len > 0) {
				length += len;
			} else {
				x++;
			}
			x++;
		}
		if (x > str.length()) {
			x = str.length();
		}

		// Add the substring to the output after cutting it

		output[0] = str.substring(0, x);

		// Add the last of the string to the output.

		output[1] = str.substring(x);
		return output;
	}

	/**
	 * Outputs a single line out, crops overflow
	 * 
	 * @param receiver
	 * @param msg
	 */
	public static void saySingle(CommandSender receiver, String msg) {
		if (receiver == null) {
			return;
		}

		receiver.sendMessage(cropRightToFit(colorize(new String[] { msg })[0],
				lineLength));
	}

	/**
	 * Outputs a message to a user
	 * 
	 * @param receiver
	 * @param msg
	 */
	public static void sendMessage(CommandSender receiver, String msg) {
		sendPrefixedMessage(receiver, null, msg);
	}

	/**
	 * Outputs a message to a user
	 * 
	 * @param receiver
	 * @param prefix
	 * @param msg
	 */
	public static void sendPrefixedMessage(CommandSender receiver,
			String prefix, String msg) {
		if (receiver == null) {
			return;
		}

		int prefix_width = prefix == null ? 0 : (int) msgLength(prefix);

		String[] message = colorize(wordWrap(msg, prefix_width));

		for (String out : message) {
			receiver.sendMessage((prefix == null ? "" : prefix + " ") + out);
		}
	}

	/**
	 * Send blank lie
	 * 
	 * @param color
	 */
	public void startColor(String color) {
		this.color = color;
	}

	/**
	 * Send blank lie
	 * 
	 * @param receiver
	 */
	public static void sendBlank(CommandSender receiver) {
		if (receiver == null) {
			return;
		}

		receiver.sendMessage(" ");
	}

	/**
	 * Colors each line
	 * 
	 * @param message
	 * @return
	 */
	public static String[] say(String message) {
		return colorize(wordWrap(message));
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	public static String[] colorize(String[] message) {
		return colorizeBase(message, 167);
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	public static String colorize(String message) {
		return colorizeBase((new String[] { message }), 167)[0];
	}

	/**
	 * 
	 * @param message
	 * @param charcode
	 * @return
	 */
	public static String[] colorizeBase(String[] message, int charcode) {
		if (message != null && message[0] != null && !message[0].isEmpty()) {
			// Go through each line

			String prevColor = "";
			String lastColor = "";

			int counter = 0;
			for (String msg : message) {
				// Loop through looking for a color code

				for (int x = 0; x < msg.length(); x++) {
					// If the char is color code
					if (msg.codePointAt(x) == charcode) {
						// advance x to the next character
						x += 1;

						lastColor = ChatColor.getByChar((char) Integer
								.parseInt(msg.charAt(x) + "", 16)) + "";
					}
				}
				// Replace the message with the colorful message

				message[counter] = prevColor + msg;
				prevColor = lastColor;
				counter++;
			}
		}

		return message;
	}

	/**
	 * 
	 * @param prefix
	 * @return
	 */
	public String firstPrefix(String prefix) {
		if (prefix_used) {
			return ChatBlock.makeEmpty(prefix);
		} else {
			prefix_used = true;
			return prefix;
		}
	}
}
