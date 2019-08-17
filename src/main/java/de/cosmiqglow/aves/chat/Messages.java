package de.cosmiqglow.aves.chat;

public enum Messages {
	
	CG_PREFIX("§8[§eCosmiqGlow§8] §7"),
	CG_PREFIX_RED("§8[§cCosmiqGlow§8] §7"),
	CG_PREFIX_BLUE("§8[§bCosmiqGlow§8] §7"),
	CG_HEADER("§e§m----------------------------------------------------"),
	CG_HEADER_RED("§c§m----------------------------------------------------"),
	CG_HEADER_GREEN("§a§m----------------------------------------------------"),
	CG_HEADER_BLUE("§b§m----------------------------------------------------"),
	NO_PERMISSIONS("§8[§eCosmiqGlow§8] §cKeine Rechte."),
	CG_ERROR_PREFIX("§8[§eCosmiqGlow§8] §cFehler: ");

	private String message;

	Messages(String msg) {
		this.message = msg;
	}

	public void setMessage(String msg) {
		this.message = msg;
	}
	
	public String getPrefix(String inside){
		return String.format("§8[§e%s§8] ", inside);
	}

	@Override
	public String toString() {
		return this.message;
	}
}
