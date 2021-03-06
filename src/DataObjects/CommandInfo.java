package DataObjects;

import Commands.AbstractCommand;
import Enums.Category;

/**
 * Created by Hans on 26-07-2016.
 */
public class CommandInfo {
    private AbstractCommand command;
    private String identifier;
    private Category category;
    private String partOf;
    private boolean isDefault;
    private boolean display;

    public CommandInfo(AbstractCommand command, Category category, String partOf, boolean isDefault, boolean display) {
        this.command = command;
        this.category = category;
        this.partOf = partOf;
        this.isDefault = isDefault;
        this.display = display;
    }

    public AbstractCommand getCommand() {
        return command;
    }

    public Category getCategory() {
        return category;
    }

    public String getPartOf() {
        return partOf;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean getDisplay() {
        return display;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
