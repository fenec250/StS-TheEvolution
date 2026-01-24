package evolutionmod.cardsV1;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.cards.GlowingCard;
import evolutionmod.orbsV1.LizardGene;
import evolutionmod.orbsV1.PlantGene;
import evolutionmod.patches.EvolutionEnum;

import java.util.List;

public class Toxin
		extends BaseEvoCard implements GlowingCard {
	public static final String cardID = "Toxin";
	public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/Toxin.png";
	private static final int COST = 1;
	private static final int POISON_FLAT = 2;
	private static final int UPGRADE_POISON_FLAT = 1;
	private static final int POISON_PERCENT_AMT = 10;
	private static final int UPGRADE_POISON_PERCENT_AMT = 5;

	public Toxin() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, EvolutionEnum.EVOLUTION_BLUE,
				CardRarity.UNCOMMON, CardTarget.ENEMY);
		this.magicNumber = this.baseMagicNumber = POISON_PERCENT_AMT;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int poison = POISON_FLAT + (upgraded ? UPGRADE_POISON_FLAT : 0);
		boolean inForm = formEffect(LizardGene.ID);
		this.exhaust = false;
		if (inForm) {
			inForm = formEffect(PlantGene.ID);
			if (inForm) {
				poison += m.maxHealth * this.magicNumber / 100;
				this.exhaust = true;
			}
		}
		addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, poison)));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Toxin();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_POISON_PERCENT_AMT);
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		if (this.customTooltips == null) {
			super.getCustomTooltips();
			this.customTooltips.add(new TooltipInfo("Form -> Form", "Channel the second Gene only if the first is present. NL Apply the effect only if both Genes are present."));
		}
		return this.customTooltips;
	}

	@Override
	public int getNumberOfGlows() {
		return 2;
	}

	@Override
	public boolean isGlowing(int glowIndex) {
		return true;
	}

	@Override
	public Color getGlowColor(int glowIndex) {
		switch (glowIndex) {
			case 0:
				return isPlayerInThisForm(LizardGene.ID) ? LizardGene.COLOR.cpy()
						: BLUE_BORDER_GLOW_COLOR.cpy();
			case 1:
				return isPlayerInThisForm(PlantGene.ID, LizardGene.ID) ? PlantGene.COLOR.cpy()
						: BLUE_BORDER_GLOW_COLOR.cpy();
			default:
				return BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}
}
