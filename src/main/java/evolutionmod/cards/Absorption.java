package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.AbsorptionPower;

import java.util.List;
import java.util.stream.Collectors;

public class Absorption
		extends BaseEvoCard implements GlowingCard {
	public static final String ID = "evolutionmod:Absorption";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/CrystalDust.png";
	private static final int COST = 1;
	private static final int ABSORB_AMT = 1;
	private static final int UPGRADE_ABSORB_AMT = 1;

	public Absorption() {
		super(ID, NAME, new RegionName("red/power/evolve"), COST, DESCRIPTION,
				CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.RARE, CardTarget.SELF);
		this.magicNumber = this.baseMagicNumber = ABSORB_AMT;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new AbstractGameAction() {
			@Override
			public void update() {
				List<AbstractOrb> orbs = p.orbs.stream()
						.filter(o -> o instanceof AbstractGene)
						.limit(magicNumber)
						.collect(Collectors.toList());
				consumeOrbs(p, orbs);

				orbs.forEach(o -> this.addToTop(new ApplyPowerAction(
						p, p, new AbsorptionPower(p, 1, ((AbstractGene) o)))));
				this.isDone = true;
			}
		});
	}

	@Override
	public void onChoseThisOption() {
		addToBot(new MakeTempCardInHandAction(this, false));
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_ABSORB_AMT);
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
//			this.upgradeBaseCost(UPGRADED_COST);
		}
	}

	@Override
	public int getNumberOfGlows() {
		return 1;
	}

	@Override
	public boolean isGlowing(int glowIndex) {
		return AbstractDungeon.player.orbs.stream().anyMatch(o -> o instanceof AbstractGene);
	}

	@Override
	public Color getGlowColor(int glowIndex) {
		switch (AbstractDungeon.player.orbs.stream()
				.filter(o -> o instanceof AbstractGene).findFirst().get().ID) {
			case HarpyGene.ID: return HarpyGene.COLOR.cpy();
			case MerfolkGene.ID: return MerfolkGene.COLOR.cpy();
			case LavafolkGene.ID: return LavafolkGene.COLOR.cpy();
			case CentaurGene.ID: return CentaurGene.COLOR.cpy();
			case LizardGene.ID: return LizardGene.COLOR.cpy();
			case BeastGene.ID: return BeastGene.COLOR.cpy();
			case PlantGene.ID: return PlantGene.COLOR.cpy();
			case ShadowGene.ID: return ShadowGene.COLOR.cpy();
			case LymeanGene.ID: return LymeanGene.COLOR.cpy();
			case InsectGene.ID: return InsectGene.COLOR.cpy();
			case SuccubusGene.ID: return SuccubusGene.COLOR.cpy();
			default: return this.glowColor;
		}
	}
}