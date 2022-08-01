package evolutionmod.cards;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Treasure extends BaseEvoCard {
	public static final String ID = "evolutionmod:Treasure";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/Treasures.png";
	private static final int COST = 0;

	public Treasure() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.UNCOMMON, CardTarget.SELF);
		this.exhaust = true;
		this.cardsToPreview = new CrystalDust(-1);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		ArrayList<AbstractCard> cardChoices = new ArrayList();
		cardChoices.add(new CrystalShield());
		cardChoices.add(new CrystalShard());
		cardChoices.add(new CrystalDust());
		if (this.upgraded) {
			Iterator var4 = cardChoices.iterator();

			while(var4.hasNext()) {
				AbstractCard c = (AbstractCard)var4.next();
				c.upgrade();
			}
		}
		this.addToBot(new ChooseOneAction(cardChoices));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Treasure();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		if (customTooltips == null) {
			super.getCustomTooltips();
			customTooltips.add(new TooltipInfo("Randomized forms",
					"The forms on the cards are selected when they are created and vary from a card to another."));
		}
		return  customTooltips;
	}

	private float previewTimer = 0f;
	private int previewIndex = 0;
	private static final List<AbstractCard> previewCards = new ArrayList<AbstractCard>() {{
		add(new CrystalShard(-1));
		add(new CrystalShield(-1));
		add(new CrystalDust(-1));
		add(inlineUpgrade(new CrystalDust(-1)));
		add(inlineUpgrade(new CrystalShard(-1)));
		add(inlineUpgrade(new CrystalShield(-1)));
	}};
	private static AbstractCard inlineUpgrade(AbstractCard card) {
		card.upgrade();
		return card;
	}

	@Override
	public void update() {
		super.update();
		if (hb.justHovered) {
			this.previewTimer = -1f;
		}
		if (hb.hovered) {
			this.previewTimer -= Gdx.graphics.getDeltaTime();
			if (previewTimer < 0) {
				previewIndex = (previewIndex + 1) % 3 + (upgraded ? 3 : 0);
				this.cardsToPreview = previewCards.get(previewIndex);
				previewTimer = 3f;
			}
		}
	}
}