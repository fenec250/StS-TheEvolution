package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import evolutionmod.orbs.LizardGene;
import evolutionmod.patches.AbstractCardEnum;

import java.util.ArrayList;

public class Stalker
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Stalker";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LizardAtt.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 4;
    private static final int POISON_AMT = 6;
    private static final int UPGRADE_POISON_AMT = 3;

    public Stalker() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = POISON_AMT;
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
        for (int i = 0; i < monsters.size(); ++i) {
            AbstractMonster monster = monsters.get(i);
            if (monster.hasPower(PoisonPower.POWER_ID)) {
                addToBot(new DamageAction(
                        monster, new DamageInfo(p, this.multiDamage[i], this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                addToBot(new ApplyPowerAction(monster, p, new PoisonPower(monster, p, this.magicNumber)));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new LizardGene()));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Stalker();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_POISON_AMT);
        }
    }

//    @Override
//    public void triggerOnGlowCheck() {
//        if (isPlayerInThisForm(LizardGene.ID)) {
//            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
//        } else {
//            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
//        }
//    }
}