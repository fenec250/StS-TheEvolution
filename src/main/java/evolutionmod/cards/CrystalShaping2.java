package evolutionmod.cards;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.CrystalShapingAction;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.CrystalShapingPower;

import java.util.ArrayList;
import java.util.List;

public class CrystalShaping2 extends BaseEvoCard {
	public static final String ID = "evolutionmod:CrystalShaping";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/CrystalStone.png";
	private static final int COST = 1;
	private static final int UPGRADED_COST = 0;
	private static final int POWER_AMT = 1;

	private static final List<AbstractCard> previewCards = new ArrayList<AbstractCard>() {{
		add(new CrystalDust(-1));
		add(new CrystalShard(-1));
		add(new CrystalShield(-1));
	}};

	private float previewTimer = 0f;
	private int previewIndex = 0;

	public CrystalShaping2() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.UNCOMMON, CardTarget.SELF);
		this.magicNumber = this.baseMagicNumber = POWER_AMT;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ApplyPowerAction(p, p, new CrystalShapingPower(p, this.magicNumber)));
	}

	@Override
	public AbstractCard makeCopy() {
		return new CrystalShaping2();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBaseCost(UPGRADED_COST);
			this.initializeDescription();
		}
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
				previewIndex = (previewIndex + 1) % 3;
				this.cardsToPreview = previewCards.get(previewIndex);
				previewTimer = 3f;
			}
		}
	}
}