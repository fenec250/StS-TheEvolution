package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.patches.AbstractCardEnum;

public class Phoenix
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:Phoenix";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LavafolkAtt.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 6;

    public Phoenix() {
        super(ID, NAME, new RegionName("red/attack/immolate"), COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new ChannelAction(new LavafolkGene()));
        boolean inForm = formEffect(HarpyGene.ID);
        if (inForm ^ upgraded) {
            this.adapt(1);
        } else if (inForm && upgraded) {
            this.adapt(p.maxOrbs);
        }
        this.useAdaptations(p, m);
    }

    @Override
    public int canAdaptWith(AbstractAdaptation adaptation) {
        return adaptation.getGeneId().equals(LavafolkGene.ID) ? adaptation.amount : 0;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.updateDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(HarpyGene.ID)) {
            this.glowColor = HarpyGene.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}