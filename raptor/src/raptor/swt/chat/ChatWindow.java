package raptor.swt.chat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import raptor.App;
import raptor.pref.PreferenceKeys;
import raptor.swt.chat.controller.MainConsoleController;

public class ChatWindow extends ApplicationWindow {
	private static final Log LOG = LogFactory.getLog(ChatWindow.class);

	ChatConsole mainConsole = null;
	CTabFolder tabFolder = null;

	public static void main(String[] args) throws Exception {
		Display display = new Display();
		App.createInstance();
		final App app = App.getInstance();
		ChatWindow window = new ChatWindow();

		// app.getFicsConnector().getPreferences().setValue(
		// PreferenceKeys.FICS_SERVER_URL, "dev.chess.sipay.ru");
		// app.getFicsConnector().getPreferences().setValue(
		// PreferenceKeys.FICS_SERVER_URL, "chess.sipay.ru");
		app.getFicsConnector().getPreferences().setValue(
				PreferenceKeys.FICS_SERVER_URL, "freechess.org");
		app.getFicsConnector().getPreferences().setValue(
				PreferenceKeys.FICS_TIMESEAL_ENABLED, true);
		app.getFicsConnector().getPreferences().setValue(
				PreferenceKeys.FICS_IS_NAMED_GUEST, false);
		app.getFicsConnector().getPreferences().setValue(
				PreferenceKeys.FICS_USER_NAME, "cday");
		display.asyncExec(new Runnable() {
			public void run() {
				app.getFicsConnector().connect();
			}
		});
		window.setBlockOnOpen(true);
		window.open();
		Display.getCurrent().dispose();
	}

	public ChatWindow() {
		super(null);
	}

	public void addChannelTab(int channel) {
	}

	public void addDirectTellTab(String personName) {
	}

	public ChatConsole getTabConsole(int index) {
		return (ChatConsole) tabFolder.getItem(index).getControl();
	}

	@Override
	protected Control createContents(Composite parent) {
		GridLayout gridLayout = new GridLayout(1, false);
		System.out.println("createdContents");

		parent.setLayout(gridLayout);
		mainConsole = new ChatConsole(parent, SWT.NONE);
		mainConsole.setController(new MainConsoleController(mainConsole));
		mainConsole.setPreferences(App.getInstance().getPreferences());
		mainConsole.setConnector(App.getInstance().getFicsConnector());
		mainConsole.createControls();
		mainConsole.getController().init();
		mainConsole.setLayoutData(new GridData(GridData.FILL_BOTH));

		// tabFolder = new CTabFolder(parent, SWT.BORDER);
		// tabFolder.setVisible(true);

		return mainConsole;
	}

	public void dispose() {
		LOG.info("Disposed ChatWindow ");
	}

	@Override
	protected void initializeBounds() {
		getShell().setSize(800, 600);
		getShell().setLocation(0, 0);
	}
}