import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class bbst {

	public static void main(String[] args) {
		RBTree tree = new RBTree();
		if (0 < args.length) {

			String inFileName = args[0];
			File nodesInputFile = new File(inFileName);
			try {
				FileReader inFile = new FileReader(nodesInputFile);
				BufferedReader in = new BufferedReader(inFile);

				String s = in.readLine();

				int nCount = Integer.parseInt(s);
				RBTree.EventNode[] nodesArr = new RBTree.EventNode[nCount];
				s = in.readLine();

				for (int i = 0; i < nCount; i++) {
					String nums[] = s.split(" ");
					int nodeID = Integer.parseInt(nums[0]);
					int nodeCount = Integer.parseInt(nums[1]);
					RBTree.EventNode node = tree.new EventNode(nodeID, nodeCount);
					node.isRed = false;
					nodesArr[i] = node;
					s = in.readLine();
				}

				tree.generateRBT(nodesArr, nCount);

				// We are creating an object of scanner class to read the command line input
				Scanner scanner = new Scanner(System.in);
				s = scanner.nextLine();
				while (!"quit".equals(s)) {
					String cmds[] = s.split(" ");
					String cmd = cmds[0];

					switch (cmd) {
					case "increase":
						tree.increase(Integer.parseInt(cmds[1]), Integer.parseInt(cmds[2]));
						break;
					case "reduce":
						tree.reduce(Integer.parseInt(cmds[1]), Integer.parseInt(cmds[2]));
						break;
					case "count":
						tree.count(Integer.parseInt(cmds[1]));
						break;
					case "inrange":
						tree.inRange(Integer.parseInt(cmds[1]), Integer.parseInt(cmds[2]));
						break;
					case "next":
						tree.next(Integer.parseInt(cmds[1]), true);
						break;
					case "previous":
						tree.previous(Integer.parseInt(cmds[1]), true);
						break;
					default:
						System.out.println("\nCommand is not valid: '" + cmd + "' ! Type 'quit' to exit. ");
						break;
					}
					s = scanner.nextLine();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("\n Enter a file name (with extension) containing the input as sorted nodes. \n");
		}
	}

}
