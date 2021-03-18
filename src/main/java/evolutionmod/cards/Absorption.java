package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
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
		extends BaseEvoCard {
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
	public void triggerOnGlowCheck() {
		if (AbstractDungeon.player.orbs.stream().anyMatch(o -> o instanceof AbstractGene)) {
			switch (AbstractDungeon.player.orbs.stream()
					.filter(o -> o instanceof AbstractGene).findFirst().get().ID) {
				case HarpyGene.ID: this.glowColor = HarpyGene.COLOR.cpy(); return;
				case MerfolkGene.ID: this.glowColor = MerfolkGene.COLOR.cpy(); return;
				case LavafolkGene.ID: this.glowColor = LavafolkGene.COLOR.cpy(); return;
				case CentaurGene.ID: this.glowColor = CentaurGene.COLOR.cpy(); return;
				case LizardGene.ID: this.glowColor = LizardGene.COLOR.cpy(); return;
				case BeastGene.ID: this.glowColor = BeastGene.COLOR.cpy(); return;
				case PlantGene.ID: this.glowColor = PlantGene.COLOR.cpy(); return;
				case ShadowGene.ID: this.glowColor = ShadowGene.COLOR.cpy(); return;
				case LymeanGene.ID: this.glowColor = LymeanGene.COLOR.cpy(); return;
				case InsectGene.ID: this.glowColor = InsectGene.COLOR.cpy(); return;
				case SuccubusGene.ID: this.glowColor = SuccubusGene.COLOR.cpy(); return;
			}
		} else {
			this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}
}